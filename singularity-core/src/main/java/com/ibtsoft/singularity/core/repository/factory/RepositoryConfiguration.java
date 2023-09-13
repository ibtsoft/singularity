package com.ibtsoft.singularity.core.repository.factory;

import com.ibtsoft.singularity.core.persistence.PersistenceUnit;

public final class RepositoryConfiguration {

  private final PersistenceUnit persistenceManager;

  private RepositoryConfiguration(final Builder builder) {
    persistenceManager = builder.persistenceManager;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private PersistenceUnit persistenceManager;

    @SuppressWarnings("checkstyle:WhitespaceAround")
    private Builder() {}

    public Builder persistenceManager(final PersistenceUnit persistenceManager) {
      this.persistenceManager = persistenceManager;
      return this;
    }

    public RepositoryConfiguration build() {
      return new RepositoryConfiguration(this);
    }
  }
}
