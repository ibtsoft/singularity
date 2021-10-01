package com.singularity.security;

import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.factory.RepositoryManagerFactory;

public class SecuredRepositoryManagerFactory extends RepositoryManagerFactory {

    @Override
    public IRepositoryManager makeRepositoryManager() {
        return new SecurityManager();
    }
}
