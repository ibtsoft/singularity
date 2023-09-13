package com.singularity.security;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.repository.FieldChange;
import com.ibtsoft.singularity.core.repository.IRepository;
import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.RepositoryCrudListener;
import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.entity.Id;

import io.reactivex.rxjava3.core.Observable;

import static java.util.stream.Collectors.toList;

public class SecuredRepository<T> implements IRepository<T>, RepositoryCrudListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuredRepository.class);

    private final UserId userId;
    private final Repository<T> repository;
    private final AclRulesRepository aclRulesRepository;

    private final List<RepositoryCrudListener> crudListeners = new CopyOnWriteArrayList<>();

    public SecuredRepository(final UserId userId, final Repository<T> repository, final AclRulesRepository aclRulesRepository) {
        this.userId = userId;
        this.repository = repository;
        this.aclRulesRepository = aclRulesRepository;

        repository.addCrudListener(this);
    }

    @Override
    public EntityValue<T> findById(final UUID id) {
        Map<UUID, AclRule> aclRules = aclRulesRepository.findByUserIdAndRepository(userId, repository.getName());
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
    public void delete(final EntityValue<T> entity) {
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
    public void addCrudListener(final RepositoryCrudListener listener) {
        crudListeners.add(listener);
    }

    @Override
    public void removeCrudListener(final RepositoryCrudListener listener) {
        crudListeners.remove(listener);
    }

    @Override
    public void onFieldChanged(final UUID id, final Object target, final Field field, final Object[] args) {
    }

    @Override
    public void deleteById(final UUID id) {
        repository.deleteById(id);
    }

    @Override
    public EntityValue<T> save(final T item) {
        EntityValue<T> entityValue = repository.save(item);
        aclRulesRepository.save(new AclRule(userId, entityValue.getRef(), true, true, true));
        fireCrudListenerAdd(entityValue);
        return entityValue;
    }

    @Override
    public Entity<T> update(final Id id, final Object... properties) {
        return repository.update(id, properties);
    }

    private void fireCrudListenerAdd(final Entity<T> entity) {
        Map<UUID, AclRule> aclRules = aclRulesRepository.findByUserIdAndRepository(userId, repository.getName());
        if (aclRules.containsKey(entity.getId()) && aclRules.get(entity.getId()).isCanView()) {
            crudListeners.forEach(repositoryCrudListener -> repositoryCrudListener.onAdd(entity));
        }
    }

    private void fireCrudListenerUpdate(final Entity<?> entity, final Field field, final Object value) {
        Map<UUID, AclRule> aclRules = aclRulesRepository.findByUserIdAndRepository(userId, repository.getName());
        if (aclRules.containsKey(entity.getId()) && aclRules.get(entity.getId()).isCanView()) {
            Observable<FieldChange> fieldChangeObservable = Observable.just(new FieldChange(entity, field, value));

            crudListeners.forEach(repositoryCrudListener ->
                fieldChangeObservable
                    .subscribe(
                        event -> repositoryCrudListener.onUpdate(event.getEntity(), event.getField(), event.getValue()),
                        error -> LOGGER.error("Failed to process field change event", error),
                        () -> LOGGER.info("Complete processing of event"))
                    .dispose());
        }
    }

    @Override
    public void onAdd(final Entity entity) {
        fireCrudListenerAdd(entity);
    }

    @Override
    public void onUpdate(final Entity<?> entity, final Field field, final Object value) {
        fireCrudListenerUpdate(entity, field, value);
    }
}
