package com.ibtsoft.singularity.core;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.RepositoriesManager;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class Singularity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Singularity.class);

    private final TransactionManager transactionManager;

    private final IRepositoryManager repositoriesManager;

    public Singularity(final SingularityConfiguration singularityConfiguration) {
        if (singularityConfiguration.getTransactionManager() != null) {
            this.transactionManager = singularityConfiguration.getTransactionManager();
        } else {
            this.transactionManager = new TransactionManager();
        }

        List<SingularityConfiguration.EntityTypeConfiguration> entityTypes = singularityConfiguration.getEntityTypes();
        if (singularityConfiguration.getRepositoryManagerFactory() != null) {
            this.repositoriesManager = singularityConfiguration.getRepositoryManagerFactory().makeRepositoryManager(entityTypes, transactionManager);
        } else {
            this.repositoriesManager = new RepositoriesManager(entityTypes, transactionManager, singularityConfiguration.getPersistenceUnit());
        }
    }

    public void addModelPackage(final String classPath) {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            LOGGER.debug("Class loader {}", cl);
            ClassPath path = ClassPath.from(cl);
            ImmutableSet<ClassInfo> classes = path.getTopLevelClassesRecursive(classPath);
            classes.forEach(classInfo -> {
                LOGGER.debug("Found class {}", classInfo.getName());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IRepositoryManager getRepositoriesManager() {
        return repositoriesManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
