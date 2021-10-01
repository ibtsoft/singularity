package com.ibtsoft.singularity.core.repository.factory;

import com.ibtsoft.singularity.core.PersistenceManager;

public class RepositoryConfiguration {

    private final PersistenceManager persistenceManager;

    private RepositoryConfiguration(Builder builder) {persistenceManager = builder.persistenceManager;}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private PersistenceManager persistenceManager;

        private Builder() {}

        public Builder persistenceManager(PersistenceManager persistenceManager) {
            this.persistenceManager = persistenceManager;
            return this;
        }

        public RepositoryConfiguration build() {
            return new RepositoryConfiguration(this);
        }
    }
}
