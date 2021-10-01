package com.singularity.security;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ibtsoft.singularity.core.Entity;
import com.ibtsoft.singularity.core.EntityValue;
import com.ibtsoft.singularity.core.IRepository;
import com.ibtsoft.singularity.core.Repository;
import com.ibtsoft.singularity.core.RepositoryCrudListener;

import static java.util.stream.Collectors.toList;

public class SecuredRepository<T> implements IRepository<T>, RepositoryCrudListener {

    private final UserId userId;
    private final Repository<T> repository;
    private final AclRulesRepository aclRulesRepository;

    private final List<RepositoryCrudListener> crudListeners = new CopyOnWriteArrayList<>();

    public SecuredRepository(UserId userId, Repository<T> repository, AclRulesRepository aclRulesRepository) {
        this.userId = userId;
        this.repository = repository;
        this.aclRulesRepository = aclRulesRepository;

        repository.addCrudListener(this);
    }

    @Override
    public EntityValue<T> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<EntityValue<T>> findAll() {
        Map<UUID, AclRule> aclRules = aclRulesRepository.findByUserIdAndRepository(userId, repository.getName());
        return aclRules.entrySet().stream()
            .filter(uuidAclRuleEntry -> uuidAclRuleEntry.getValue().isCanView())
            .map(uuidAclRuleEntry -> repository.findById(uuidAclRuleEntry.getKey()))
            .collect(toList());
    }

    @Override
    public void delete(EntityValue<T> entity) {
        repository.delete(entity);
    }

    @Override
    public Class<T> getRepositoryClass() {
        return repository.getRepositoryClass();
    }

    @Override
    public String getName() {
        return getRepositoryClass().getSimpleName();
    }

    @Override
    public void addCrudListener(RepositoryCrudListener listener) {
        crudListeners.add(listener);
    }

    @Override
    public void removeCrudListener(RepositoryCrudListener listener) {
        crudListeners.remove(listener);
    }

    @Override
    public EntityValue<T> update(EntityValue<T> entity) {
        return repository.update(entity);
    }

    @Override
    public EntityValue<T> save(T item) {
        EntityValue<T> entityValue = repository.save(item);
        aclRulesRepository.save(new AclRule(userId, entityValue.getRef(repository), true, true, true));
        fireCrudListenerAdd(entityValue);
        return entityValue;
    }

    private void fireCrudListenerAdd(Entity<T> entity) {
        Map<UUID, AclRule> aclRules = aclRulesRepository.findByUserIdAndRepository(userId, repository.getName());
        if (aclRules.containsKey(entity.getId()) && aclRules.get(entity.getId()).isCanView()) {
            crudListeners.forEach(repositoryCrudListener -> repositoryCrudListener.onAdd(entity));
        }
    }

    @Override
    public void onAdd(Entity entity) {
        fireCrudListenerAdd(entity);
    }
}
