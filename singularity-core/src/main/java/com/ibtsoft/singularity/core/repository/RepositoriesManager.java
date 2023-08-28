package com.ibtsoft.singularity.core.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.ibtsoft.singularity.core.SingularityConfiguration;
import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.persistence.PersistenceUnit;
import com.ibtsoft.singularity.core.repository.entity.Id;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

import static java.lang.String.format;

public class RepositoriesManager implements IRepositoryManager {

    private final Map<String, Repository<?>> repositories = new ConcurrentHashMap<>();

    private final List<SingularityConfiguration.EntityTypeConfiguration> entityTypes;
    private final PersistenceUnit persistenceUnit;
    private final TransactionManager transactionManager;

    private final EntityStructureCache entityStructureCache;

    public RepositoriesManager(List<SingularityConfiguration.EntityTypeConfiguration> entityTypes, TransactionManager transactionManager) {
        this(entityTypes, transactionManager, null);
    }

    public RepositoriesManager(List<SingularityConfiguration.EntityTypeConfiguration> entityTypes, TransactionManager transactionManager,
        Function<EntityStructureCache, PersistenceUnit> persistenceUnit) {
        assert entityTypes != null;
        assert transactionManager != null;

        this.entityTypes = entityTypes;
        this.transactionManager = transactionManager;

        this.entityStructureCache = new EntityStructureCache(entityTypes);

        this.persistenceUnit = persistenceUnit.apply(this.entityStructureCache);
        if (this.persistenceUnit != null) {
            this.persistenceUnit.connect();
        }

        entityTypes.forEach(entityType -> {
            if (entityType.getRepositoryClass() == null) {
                createRepository(entityType.getEntityType());
            } else {
                createRepository(entityType.getRepositoryClass(), entityType.getEntityType());
            }
        });
    }

    public Repository<?> getRepository(String repository) {
        if (!repositories.containsKey(repository)) {
            throw new RuntimeException(format("Cannot get repository by name %s", repository));
        }
        return repositories.get(repository);
    }

    @Override
    public <T> Repository<T> getRepository(RepositoryDescriptor repositoryDescriptor) {
        return (Repository<T>) repositories.get(repositoryDescriptor.getRepositoryName());
    }

    @Override
    public <T, R extends Repository<T>> R getRepository(Class<R> repositoryClass, Class<T> entityClass) {
        return (R) repositories.get(entityClass.getSimpleName());
    }

    private void addRepository(Repository<?> repository) {
        repositories.put(repository.getRepositoryClass().getSimpleName(), repository);
    }

    public <T> Repository<T> createRepository(Class<T> modelClass) {
        Repository<T> repository;
        if (persistenceUnit != null) {
            repository = new Repository<T>(modelClass, entityStructureCache, transactionManager, persistenceUnit.getPersistence(modelClass));
        } else {
            repository = new Repository<>(modelClass, entityStructureCache, transactionManager);
        }
        addRepository(repository);
        repository.init();
        return repository;
    }

    private <T> Repository<T> createRepository(Class<T> repositoryClass, Class<T> modelClass) {
        Repository<T> repository;
        try {
            if (persistenceUnit != null) {
                repository = (Repository<T>) repositoryClass
                    .getConstructor(Class.class, EntityStructureCache.class, TransactionManager.class, Persistence.class)
                    .newInstance(modelClass, entityStructureCache, transactionManager, persistenceUnit.getPersistence(modelClass));
            } else {
                repository = (Repository<T>) repositoryClass
                    .getConstructor(Class.class, EntityStructureCache.class, TransactionManager.class)
                    .newInstance(modelClass, entityStructureCache, transactionManager);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find constructor for repository ", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate repository ", e);
        }
        addRepository(repository);
        repository.init();
        return repository;
    }

    public Id getId(Object object) {
        if (entityTypes.stream().anyMatch(entityType -> object.getClass().equals(entityType.getEntityType()))) {
            return Id.generate();
        } else {
            throw new RuntimeException("This is not a managed entity");
        }
    }
}
