package com.ibtsoft.singularity.core.repository;

public interface IRepositoryManager {

    <T> IRepository<T> getRepository(RepositoryDescriptor repositoryDescriptor);

    <T, R extends Repository<T>> R getRepository(Class<R> repositoryClass, Class<T> entityClass);
}
