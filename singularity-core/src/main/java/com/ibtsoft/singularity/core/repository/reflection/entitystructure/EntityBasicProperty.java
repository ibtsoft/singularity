package com.ibtsoft.singularity.core.repository.reflection.entitystructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityBasicProperty extends EntityProperty {

    private final Method getter;
    private final Method setter;

    public EntityBasicProperty(Field field, Method getter, Method setter) {
        super(field);
        this.getter = getter;
        this.setter = setter;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }
}
