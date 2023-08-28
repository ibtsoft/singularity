package com.ibtsoft.singularity.core.repository;

import java.lang.reflect.Field;

import com.ibtsoft.singularity.core.repository.entity.Entity;

public interface RepositoryCrudListener {

    void onAdd(Entity<?> entity);

    void onUpdate(Entity<?> entity, Field field, Object value);
}
