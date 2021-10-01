package com.ibtsoft.singularity.core.repository.factory;

import com.ibtsoft.singularity.core.Repository;

public interface RepositoryFactory {

    Repository<?> makeRepository(RepositoryConfiguration repositoryConfiguration);
}
