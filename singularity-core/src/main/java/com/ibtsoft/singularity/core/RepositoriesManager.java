package com.ibtsoft.singularity.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibtsoft.singularity.core.repository.IRepositoryManager;
import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;

public class RepositoriesManager implements IRepositoryManager {

    Map<String, Repository<?>> repositories = new ConcurrentHashMap<>();

    public Repository<?> getRepository(String repository) {
        return repositories.get(repository);
    }

    private void addRepository(Repository<?> repository) {
        repositories.put(repository.getRepositoryClass().getSimpleName(), repository);
    }

    @Override
    public Repository<?> getRepository(RepositoryDescriptor repositoryDescriptor) {
        return repositories.get(repositoryDescriptor.getRepositoryName());
    }

    @Override
    public <T> Repository<T> createRepository(Class<T> modelClass) {
        Repository<T> repository = new Repository<>(modelClass);
        addRepository(repository);
        return repository;
    }
}
