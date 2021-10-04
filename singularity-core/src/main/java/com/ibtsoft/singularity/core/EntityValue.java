package com.ibtsoft.singularity.core;

import java.util.UUID;

public class EntityValue<T> extends Entity<T>{

    private final T value;

    public EntityValue(UUID id, String entityClass, T value) {
        super(id, entityClass);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public EntityRef<T> getRef(Repository<T> repository) {
        return new EntityRef<>(getId(), getEntityClass(), repository);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityValue{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
