package com.singularity.security;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ibtsoft.singularity.core.SingularityConfiguration.EntityTypeConfiguration;
import com.ibtsoft.singularity.core.repository.IRepository;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.RepositoriesManager;
import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;
import com.ibtsoft.singularity.core.repository.entity.EntityRef;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;
import com.singularity.security.excepiton.UserNotFoundException;
import com.singularity.security.token.Token;
import com.singularity.security.token.TokenRepository;

import static java.lang.String.format;

public class SecurityManager implements IRepositoryManager {

    private final Map<String, UUID> users = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final AclRulesRepository aclRulesRepository;
    private final RepositoriesManager repositoriesManager;

    private final TokenRepository tokenRepository;

    private final Map<RepositoryUsername, SecuredRepository<?>> securedRepositories = new ConcurrentHashMap<>();

    public SecurityManager(List<EntityTypeConfiguration> entityTypes, TransactionManager transactionManager) {
        List<EntityTypeConfiguration> allEntityTypes = new ArrayList<>(entityTypes);
        allEntityTypes.add(new EntityTypeConfiguration(User.class, UserRepository.class));
        allEntityTypes.add(new EntityTypeConfiguration(AclRule.class, AclRulesRepository.class));
        allEntityTypes.add(new EntityTypeConfiguration(Token.class, TokenRepository.class));
        this.repositoriesManager = new RepositoriesManager(allEntityTypes, transactionManager);

        this.userRepository = this.repositoriesManager.getRepository(UserRepository.class, User.class);
        this.aclRulesRepository = this.repositoriesManager.getRepository(AclRulesRepository.class, AclRule.class);

        this.tokenRepository = this.repositoriesManager.getRepository(TokenRepository.class, Token.class);
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

    public LoginResult login(String token) {
        Optional<EntityValue<Token>> tokenEntityValue = tokenRepository.findByTokenValue(token);
        if (tokenEntityValue.isPresent() && tokenEntityValue.get().getValue().getExpiration().isAfter(LocalDateTime.now())) {
            EntityValue<User> user = getUserByUsername(tokenEntityValue.get().getValue().getUsername());
            return LoginResult.success(user.getValue().getUsername(), UserId.forUUID(user.getId()), tokenRepository.generateToken(user.getValue().getUsername(),
                tokenEntityValue.get().getId()));
        } else {
            return LoginResult.wrongToken();
        }
    }

    public LoginResult login(String username, String password) {
        Optional<EntityValue<User>> user = findUserByUsername(username);
        if (user.isPresent()) {
            if (user.get().getValue().getPassword().equals(password)) {
                return LoginResult.success(username, UserId.forUUID(user.get().getId()), tokenRepository.generateToken(user.get().getValue().getUsername()));
            } else {
                return LoginResult.wrongPassword(username);
            }
        } else {
            return LoginResult.wrongUsername(username);
        }
    }

    @Override
    public <T> IRepository<T> getRepository(RepositoryDescriptor repositoryDescriptor) {
        if (!(repositoryDescriptor instanceof SecuredRepositoryDescriptor)) {
            throw new RuntimeException("SecuredRepositoryManager requires  SecuredRepositoryDescriptor to get a repository");
        }
        SecuredRepositoryDescriptor securedRepositoryDescriptor = ((SecuredRepositoryDescriptor) repositoryDescriptor);
        String repository = securedRepositoryDescriptor.getRepositoryName();
        UserId userId = securedRepositoryDescriptor.getUserId();
        return (IRepository<T>) securedRepositories.computeIfAbsent(new RepositoryUsername(repository, userId),
            repositoryUsername -> new SecuredRepository<>(userId, repositoriesManager.getRepository(repositoryUsername.getRepository()),
                aclRulesRepository));
    }

    public <T> SecuredRepository<T> getRepository(Class<T> repositoryClass, UserId username) {
        return (SecuredRepository<T>) getRepository(repositoryClass.getSimpleName(), username);
    }

    public SecuredRepository<?> getRepository(String repository, UserId username) {
        Repository<?> repo = repositoriesManager.getRepository(repository);
        return securedRepositories.computeIfAbsent(new RepositoryUsername(repository, username),
            repositoryUsername -> new SecuredRepository<>(username, repo, aclRulesRepository));
    }

    @Override
    public <T, R extends Repository<T>> R getRepository(Class<R> repositoryClass, Class<T> entityClass) {
        return repositoriesManager.getRepository(repositoryClass, entityClass);
    }

    public EntityValue<User> getUserByUsername(String username) {
        return findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private Optional<EntityValue<User>> findUserByUsername(String username) {
        UUID id = users.get(username);
        return id != null ? Optional.of(userRepository.findById(id)) : Optional.empty();
    }

    /*public <T> Repository<T> createRepository(Class<T> modelClass) {
        return repositoriesManager.createRepository(modelClass);
    }*/

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void addAcl(UserId userId, EntityRef<?> entity, boolean isCreator, boolean canView, boolean canChange) {
        aclRulesRepository.save(new AclRule(userId, entity, isCreator, canView, canChange));
    }
}
