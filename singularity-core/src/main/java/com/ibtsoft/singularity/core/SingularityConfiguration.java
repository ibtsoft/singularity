package com.ibtsoft.singularity.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ibtsoft.singularity.core.persistence.PersistenceUnit;
import com.ibtsoft.singularity.core.repository.factory.RepositoryManagerFactory;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public final class SingularityConfiguration {

  private final List<EntityTypeConfiguration> entityTypes;
  private final RepositoryManagerFactory repositoryManagerFactory;
  private final Function<EntityStructureCache, PersistenceUnit> persistenceUnit;

  private final TransactionManager transactionManager;

  private SingularityConfiguration(final Builder builder) {
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

  public Function<EntityStructureCache, PersistenceUnit> getPersistenceUnit() {
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

    @SuppressWarnings("checkstyle:WhitespaceAround")
    private Builder() {}

    public Builder repositoryManagerFactory(
        final RepositoryManagerFactory repositoryManagerFactory) {
      this.repositoryManagerFactory = repositoryManagerFactory;
      return this;
    }

    public Builder persistenceUnit(
        final Function<EntityStructureCache, PersistenceUnit> persistenceUnit) {
      this.persistenceUnit = persistenceUnit;
      return this;
    }

    public Builder withEntityType(final Class entityType) {
      this.entityTypes.add(new EntityTypeConfiguration(entityType));
      return this;
    }

    public Builder transactionManager(final TransactionManager transactionManager) {
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

    public EntityTypeConfiguration(final Class entityType) {
      this.entityType = entityType;
      this.repositoryClass = null;
    }

    public EntityTypeConfiguration(final Class entityType, final Class repositoryClass) {
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
