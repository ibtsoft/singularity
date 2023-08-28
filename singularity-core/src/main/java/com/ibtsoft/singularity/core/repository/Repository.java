package com.ibtsoft.singularity.core.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.entity.Id;
import com.ibtsoft.singularity.core.repository.exception.EntityNotFoundException;
import com.ibtsoft.singularity.core.repository.reflection.RepoInvocationHandler;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

import io.reactivex.rxjava3.core.Observable;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

public class Repository<T> implements IRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    private final Class<T> repositoryClass;

    private final EntityStructureCache entityStructureCache;

    private final Persistence<T> persistence;

    private final ProxyFactory proxyFactory;

    private final TransactionManager transactionManager;

    protected final Map<UUID, EntityValue<T>> items = new ConcurrentHashMap<>();

    protected final Map<UUID, EntityValue<T>> deletedItems = new ConcurrentHashMap<>();

    private final List<RepositoryCrudListener> crudListeners = new CopyOnWriteArrayList<>();

    public Repository(Class<T> repositoryClass, EntityStructureCache entityStructureCache, TransactionManager transactionManager) {
        this(repositoryClass, entityStructureCache, transactionManager, null);
    }

    public Repository(Class<T> repositoryClass, EntityStructureCache entityStructureCache, TransactionManager transactionManager, Persistence<T> persistence) {
        assert repositoryClass != null;
        assert entityStructureCache != null;
        assert transactionManager != null;

        this.repositoryClass = repositoryClass;
        this.entityStructureCache = entityStructureCache;
        this.persistence = persistence;
        this.transactionManager = transactionManager;

        proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(getRepositoryClass());

        proxyFactory.setFilter(m -> {
            // ignore finalize()
            return !m.getName().equals("finalize");
        });
    }

    public void init() {
        if (persistence != null) {
            persistence.loadAll().forEach(entity -> items.put(entity.getId(), entity));
        }
    }

    @Override
    public EntityValue<T> findById(UUID id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new EntityNotFoundException(getName(), Id.forUUID(id));
        }
    }

    @Override
    public List<EntityValue<T>> findAll() {
        return new ArrayList<>(items.values());
    }

    protected void put(EntityValue<T> item) {
        items.put(item.getId(), item);
    }

    @Override
    public EntityValue<T> save(T item) {
        UUID id = UUID.randomUUID();

        T proxiedItem = null;
        try {
            proxiedItem = proxy(id, item);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error("Failed to create proxy for entity type {}, entity {}", getRepositoryClass().getSimpleName(), item);
            throw new RuntimeException(String.format("Failed to create proxy for entity type %s, entity %s", getRepositoryClass().getSimpleName(), item), e);
        }

        EntityValue<T> entity = new EntityValue<T>(id, this.getName(), proxiedItem);
        if (persistence != null) {
            persistence.save(entity);
        }

        items.put(id, entity);
        LOGGER.debug("Added entity: {}", entity);
        fireCrudListenerAdd(entity);
        return entity;
    }

    @Override
    public Entity<T> update(Id id, Object... properties) {
        Entity<T> entity = findById(id.getUuid());

        return null;
    }

    @Override
    public void deleteById(UUID id) {
        deletedItems.put(id, items.remove(id));
        if (persistence != null) {
            persistence.remove(id);
        }
        LOGGER.debug("Deleted entity, id: {}", id);
    }

    @Override
    public void delete(EntityValue<T> entity) {
        deleteById(entity.getId());
    }

    public Class<T> getRepositoryClass() {
        return repositoryClass;
    }

    @Override
    public String getName() {
        return repositoryClass.getSimpleName();
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
    public void onFieldChanged(UUID id, Object target, Field field, Object[] args) {
        Observable<FieldChange> fieldChangeObservable = Observable.just(new FieldChange(findById(id), field, args));
        crudListeners.forEach(repositoryCrudListener ->
            fieldChangeObservable
                .subscribe(
                    event -> repositoryCrudListener.onUpdate(event.getEntity(), event.getField(), event.getValue()),
                    error -> LOGGER.error("Failed to process field change event", error),
                    () -> LOGGER.info("Complete processing of event"))
                .dispose());
    }

    private void fireCrudListenerAdd(Entity<T> entity) {
        crudListeners.forEach(repositoryCrudListener -> repositoryCrudListener.onAdd(entity));
    }

    /*  protected Entity<T> add(Class<T> clazz) {
          UUID id = UUID.randomUUID();
          Entity<T> entity = null;
          try {
              entity = new Entity<>(id, proxy(clazz, id));
          } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
              LOGGER.error("Failed to create proxy", e);
              return null;
          }
          items.put(id, entity);
          return entity;
      }
    */

    private T proxy(UUID id, T item) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        RepoInvocationHandler mi = new RepoInvocationHandler(id, item, entityStructureCache, this, transactionManager);
        Object result = proxyFactory.create(new Class[0], new Object[0], mi);
        Proxy proxy = (Proxy) result;
        // result.
        return (T) result;
    }
}
