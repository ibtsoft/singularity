package com.ibtsoft.singularity.core.repository.reflection.entitystructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityListProperty extends EntityProperty {

    private final Method getter;
    private final Method add;
    private final Method remove;

    public EntityListProperty(final Field field, final Method getter, final Method add, final Method remove) {
        super(field);
        this.getter = getter;
        this.add = add;
        this.remove = remove;
    }
}
