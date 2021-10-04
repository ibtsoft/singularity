package com.singularity.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ibtsoft.singularity.core.EntityValue;
import com.ibtsoft.singularity.core.IRepository;
import com.ibtsoft.singularity.core.RepositoriesManager;
import com.ibtsoft.singularity.core.Repository;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;

import static java.lang.String.format;

public class SecurityManager implements IRepositoryManager {

    private final Map<String, UUID> users = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final AclRulesRepository aclRulesRepository;
    private final RepositoriesManager repositoriesManager;

    private final Map<RepositoryUsername, SecuredRepository<?>> securedRepositories = new ConcurrentHashMap<>();

    public SecurityManager() {
        this.userRepository = new UserRepository(null);
        this.aclRulesRepository = new AclRulesRepository(null);
        this.repositoriesManager = new RepositoriesManager();
    }

    public void init() {
        userRepository.init();
        userRepository.findAll().forEach(userEntity -> users.put(userEntity.getValue().getUsername(), userEntity.getId()));
    }

    public EntityValue<User> addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new RuntimeException(format("User with username %s already exists", user.getUsername()));
        }
        EntityValue<User> userEntity = userRepository.save(user);
        users.put(user.getUsername(), userEntity.getId());
        return userEntity;
    }

    public LoginResult login(String username, String password) {
        EntityValue<User> user = getUserByUsername(username);
        if (user != null) {
            if (user.getValue().getPassword().equals(password)) {
                return LoginResult.success(username, UserId.forUUID(user.getId()));
            } else {
                return LoginResult.wrongPassword(username);
            }
        } else {
            return LoginResult.wrongUsername(username);
        }
    }

    public <T> SecuredRepository<T> getRepository(Class<T> repositoryClass, UserId username) {
        return (SecuredRepository<T>) getRepository(repositoryClass.getSimpleName(), username);
    }

    public SecuredRepository<?> getRepository(String repository, UserId username) {
        return securedRepositories.computeIfAbsent(new RepositoryUsername(repository, username),
            repositoryUsername -> new SecuredRepository<>(username, repositoriesManager.getRepository(repositoryUsername.getRepository()), aclRulesRepository));
    }

    private EntityValue<User> getUserByUsername(String username) {
        UUID id = users.get(username);
        if (id != null) {
            return userRepository.findById(id);
        } else {
            return null;
        }
    }

    @Override
    public IRepository<?> getRepository(RepositoryDescriptor repositoryDescriptor) {
        if (!(repositoryDescriptor instanceof SecuredRepositoryDescriptor)) {
            throw new RuntimeException("SecuredRepositoryManager requires  SecuredRepositoryDescriptor to get a repository");
        }
        SecuredRepositoryDescriptor securedRepositoryDescriptor = ((SecuredRepositoryDescriptor) repositoryDescriptor);
        String repository = securedRepositoryDescriptor.getRepositoryName();
        UserId userId = securedRepositoryDescriptor.getUserId();
        return securedRepositories.computeIfAbsent(new RepositoryUsername(repository, userId),
            repositoryUsername -> new SecuredRepository<>(userId, repositoriesManager.getRepository(repositoryUsername.getRepository()),
                aclRulesRepository));
    }

    @Override
    public <T> Repository<T> createRepository(Class<T> modelClass) {
        return repositoriesManager.createRepository(modelClass);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
