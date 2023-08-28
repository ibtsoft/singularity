package com.ibtsoft.singularity.core.repository.entity;

import java.util.UUID;

public abstract class Entity<T> {

    private final UUID id;
    private final String entityClass;

    public Entity(UUID id, String entityClass) {
        this.id = id;
        this.entityClass = entityClass;
    }

    public UUID getId() {
        return id;
    }

    public String getEntityClass() {
        return entityClass;
    }
}
