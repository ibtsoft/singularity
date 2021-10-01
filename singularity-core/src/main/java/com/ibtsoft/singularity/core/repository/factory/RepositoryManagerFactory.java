package com.ibtsoft.singularity.core.repository.factory;

import com.ibtsoft.singularity.core.RepositoriesManager;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;

public class RepositoryManagerFactory {

    public IRepositoryManager makeRepositoryManager() {
        return new RepositoriesManager();
    }
}
