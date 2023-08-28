package com.ibtsoft.singularity.core.repository.entity;

import java.util.UUID;

public class EntityRef<T> extends Entity<T> {

    public EntityRef(UUID id, String entityClassName) {
        super(id, entityClassName);
    }
}
