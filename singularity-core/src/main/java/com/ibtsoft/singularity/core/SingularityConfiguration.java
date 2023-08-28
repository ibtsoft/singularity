package com.ibtsoft.singularity.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ibtsoft.singularity.core.persistence.PersistenceUnit;
import com.ibtsoft.singularity.core.repository.factory.RepositoryManagerFactory;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class SingularityConfiguration {

    private final List<EntityTypeConfiguration> entityTypes;
    private final RepositoryManagerFactory repositoryManagerFactory;
    private final Function<EntityStructureCache, PersistenceUnit> persistenceUnit;

    private final TransactionManager transactionManager;

    private SingularityConfiguration(Builder builder) {
        this.entityTypes = builder.entityTypes;
        this.repositoryManagerFactory = builder.repositoryManagerFactory;
        this.persistenceUnit = builder.persistenceUnit;

        this.transactionManager = builder.transactionManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<EntityTypeConfiguration> getEntityTypes() {
        return entityTypes;
    }

    public RepositoryManagerFactory getRepositoryManagerFactory() {
        return repositoryManagerFactory;
    }

    public Function<EntityStructureCache, PersistenceUnit>  getPersistenceUnit() {
        return persistenceUnit;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public static final class Builder {

        private final List<EntityTypeConfiguration> entityTypes = new ArrayList<>();
        private RepositoryManagerFactory repositoryManagerFactory;
        private Function<EntityStructureCache, PersistenceUnit> persistenceUnit;

        private TransactionManager transactionManager;

        private Builder() {}

        public Builder repositoryManagerFactory(RepositoryManagerFactory repositoryManagerFactory) {
            this.repositoryManagerFactory = repositoryManagerFactory;
            return this;
        }

        public Builder persistenceUnit(Function<EntityStructureCache, PersistenceUnit> persistenceUnit) {
            this.persistenceUnit = persistenceUnit;
            return this;
        }

        public Builder withEntityType(Class entityType) {
            this.entityTypes.add(new EntityTypeConfiguration(entityType));
            return this;
        }

        public Builder transactionManager(TransactionManager transactionManager) {
            this.transactionManager = transactionManager;
            return this;
        }

        public SingularityConfiguration build() {
            return new SingularityConfiguration(this);
        }
    }

    public static final class EntityTypeConfiguration {

        private final Class entityType;
        private final Class repositoryClass;

        public EntityTypeConfiguration(Class entityType) {
            this.entityType = entityType;
            this.repositoryClass = null;
        }

        public EntityTypeConfiguration(Class entityType, Class repositoryClass) {
            this.entityType = entityType;
            this.repositoryClass = repositoryClass;
        }

        public Class getEntityType() {
            return entityType;
        }

        public Class getRepositoryClass() {
            return repositoryClass;
        }
    }
}
