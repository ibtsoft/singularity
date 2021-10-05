package com.ibtsoft.singularity.core;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Repository<T> implements IRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);

    private final Class<T> repositoryClass;

    private final Persistence<T> persistence;

    private final ProxyFactory proxyFactory;

    protected final Map<UUID, EntityValue<T>> items;

    protected final Map<UUID, EntityValue<T>> deletedItems;

    private final List<RepositoryCrudListener> crudListeners = new CopyOnWriteArrayList<>();

    public Repository(Class<T> repositoryClass) {
        this(repositoryClass, null);
    }

    public Repository(Class<T> repositoryClass, Persistence<T> persistence) {
        this.repositoryClass = repositoryClass;
        this.persistence = persistence;

        items = new ConcurrentHashMap<>();
        deletedItems = new ConcurrentHashMap<>();

        proxyFactory = new ProxyFactory();

        proxyFactory.setFilter(new MethodFilter() {
            public boolean isHandled(Method m) {
                // ignore finalize()
                return !m.getName().equals("finalize");
            }
        });
    }

    public void init() {
        if (persistence != null) {
            persistence.loadAll().forEach(entity -> items.put(entity.getId(), entity));
        }
    }

    @Override
    public EntityValue<T> findById(UUID id) {
        return items.get(id);
    }

    @Override
    public List<EntityValue<T>> findAll() {
        return new ArrayList<>(items.values());
    }

    protected void put(EntityValue<T> item) {
        items.put(item.getId(), item);
    }

    /*    protected void add(EntityValue<T> item) {
            if (items.containsKey(item.getId())) {
                throw new RuntimeException("Entity with this key already exists");
            }
            items.put(item.getId(), item);


        }
    */
    @Override
    public EntityValue<T> save(T item) {
        UUID id = UUID.randomUUID();

        EntityValue<T> entity = new EntityValue<T>(id, item.getClass().getSimpleName(), item);
        if (persistence != null) {
            persistence.save(entity);
        }

        items.put(id, entity);
        LOGGER.debug("Added entity: {}", entity);
        fireCrudListenerAdd(entity);
        return entity;
    }

    @Override
    public EntityValue<T> update(EntityValue<T> entity) {
        put(entity);
        if (persistence != null) {
            persistence.save(entity);
        }
        LOGGER.debug("Updated entity: {}", entity);
        return entity;
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

      private T proxy(Class<T> clazz, UUID id) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
          proxyFactory.setSuperclass(clazz);

          MethodHandler mi = new UpdateMethodHandler(this, id);

          return (T) proxyFactory.create(new Class[0], new Object[0], mi);
      }
  */
    protected void fireObjectChanged(UUID id, String methodName) {

    }
}
