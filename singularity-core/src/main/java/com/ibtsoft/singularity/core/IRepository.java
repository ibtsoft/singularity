package com.ibtsoft.singularity.core;

import java.util.List;
import java.util.UUID;

public interface IRepository<T> {

    EntityValue<T> findById(UUID id);

    List<EntityValue<T>> findAll();

    EntityValue<T> save(T item);

    EntityValue<T> update(EntityValue<T> entity);

    void delete(EntityValue<T> entity);

    Class<T> getRepositoryClass();

    String getName();

    void addCrudListener(RepositoryCrudListener listener);

    void removeCrudListener(RepositoryCrudListener listener);
}
