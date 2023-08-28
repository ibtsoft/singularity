package com.ibtsoft.singularity.core.repository;

import java.lang.reflect.Field;

import com.ibtsoft.singularity.core.repository.entity.Entity;

public class FieldChange {

    private final Entity<?> entity;
    private final Field field;
    private final Object value;

    public FieldChange(Entity<?> entity, Field field, Object value) {
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public Field getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
