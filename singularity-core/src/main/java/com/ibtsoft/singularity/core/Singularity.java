package com.ibtsoft.singularity.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.ibtsoft.singularity.core.repository.IRepositoryManager;

public class Singularity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Singularity.class);

    private final List<Class<?>> models = new ArrayList<>();

    private PersistenceManager persistenceManager;

    private final IRepositoryManager repositoriesManager;

    public Singularity(SingularityConfiguration singularityConfiguration) {
        if(singularityConfiguration.getRepositoryManagerFactory()!=null) {
            this.repositoriesManager = singularityConfiguration.getRepositoryManagerFactory().makeRepositoryManager();
        } else {
            this.repositoriesManager = new RepositoriesManager();
        }
    }

    public void addModelPackage(String classPath) {
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
    public <T> Repository<T> createRepository(Class<T> modelClass) {
        return repositoriesManager.createRepository(modelClass);
    }

    public IRepositoryManager getRepositoriesManager() {
        return repositoriesManager;
    }
}
