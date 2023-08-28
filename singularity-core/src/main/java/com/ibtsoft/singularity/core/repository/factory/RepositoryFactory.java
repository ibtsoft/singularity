package com.ibtsoft.singularity.core.repository.factory;

import com.ibtsoft.singularity.core.repository.Repository;

public interface RepositoryFactory {

    Repository<?> makeRepository(RepositoryConfiguration repositoryConfiguration);
}
