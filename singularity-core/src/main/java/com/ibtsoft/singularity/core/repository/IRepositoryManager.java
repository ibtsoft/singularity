package com.ibtsoft.singularity.core.repository;

import com.ibtsoft.singularity.core.IRepository;
import com.ibtsoft.singularity.core.Repository;

public interface IRepositoryManager {

    IRepository<?> getRepository(RepositoryDescriptor repositoryDescriptor);

    <T> Repository<T> createRepository(Class<T> modelClass);
}
