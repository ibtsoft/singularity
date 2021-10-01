package com.ibtsoft.singularity.core;

import com.ibtsoft.singularity.core.repository.factory.RepositoryManagerFactory;

public class SingularityConfiguration {

    private final RepositoryManagerFactory repositoryManagerFactory;

    private SingularityConfiguration(Builder builder) {repositoryManagerFactory = builder.repositoryManagerFactory;}

    public static Builder builder() {
        return new Builder();
    }

    public RepositoryManagerFactory getRepositoryManagerFactory() {
        return repositoryManagerFactory;
    }

    public static final class Builder {

        private RepositoryManagerFactory repositoryManagerFactory;

        private Builder() {}

        public Builder repositoryManagerFactory(RepositoryManagerFactory repositoryManagerFactory) {
            this.repositoryManagerFactory = repositoryManagerFactory;
            return this;
        }

        public SingularityConfiguration build() {
            return new SingularityConfiguration(this);
        }
    }
}
