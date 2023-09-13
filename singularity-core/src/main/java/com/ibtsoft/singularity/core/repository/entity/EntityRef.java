package com.ibtsoft.singularity.core.repository.entity;

import java.util.UUID;

public class EntityRef<T> extends Entity<T> {

    public EntityRef(final UUID id, final String entityClassName) {
        super(id, entityClassName);
    }
}
