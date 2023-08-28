package com.ibtsoft.singularity.core.repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.entity.Id;

public interface IRepository<T> {

    EntityValue<T> findById(UUID id);

    List<EntityValue<T>> findAll();

    EntityValue<T> save(T item);

    Entity<T> update(Id id, Object... properties);

    void deleteById(UUID id);

    void delete(EntityValue<T> entity);

    Class<T> getRepositoryClass();

    String getName();

    void addCrudListener(RepositoryCrudListener listener);

    void removeCrudListener(RepositoryCrudListener listener);

    void onFieldChanged(UUID id, Object target, Field field, Object[] args);
}
