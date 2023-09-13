package com.ibtsoft.singularity.core.repository.factory;

import java.util.List;

import com.ibtsoft.singularity.core.SingularityConfiguration;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.RepositoriesManager;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class RepositoryManagerFactory {

    public IRepositoryManager makeRepositoryManager(final List<SingularityConfiguration.EntityTypeConfiguration> entityTypes,
        final TransactionManager transactionManager) {
        return new RepositoriesManager(entityTypes, transactionManager);
    }
}
