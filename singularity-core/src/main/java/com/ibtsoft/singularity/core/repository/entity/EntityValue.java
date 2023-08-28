package com.ibtsoft.singularity.core.repository.entity;

import java.util.UUID;

public class EntityValue<T> extends Entity<T> {

    private final T value;

    public EntityValue(UUID id, String entityClass, T value) {
        super(id, entityClass);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public EntityRef<T> getRef() {
        return new EntityRef<>(getId(), getEntityClass());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityValue{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
