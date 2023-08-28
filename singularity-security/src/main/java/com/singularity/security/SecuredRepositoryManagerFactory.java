package com.singularity.security;

import java.util.List;

import com.ibtsoft.singularity.core.SingularityConfiguration;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.factory.RepositoryManagerFactory;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class SecuredRepositoryManagerFactory extends RepositoryManagerFactory {

    @Override
    public IRepositoryManager makeRepositoryManager(List<SingularityConfiguration.EntityTypeConfiguration> entityTypes,
        TransactionManager transactionManager) {
        return new SecurityManager(entityTypes, transactionManager);
    }
}
