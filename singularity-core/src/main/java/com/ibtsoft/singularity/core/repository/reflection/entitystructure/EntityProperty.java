package com.ibtsoft.singularity.core.repository.reflection.entitystructure;

import java.lang.reflect.Field;

public abstract class EntityProperty {

    private final Field field;

    protected EntityProperty(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }
}
