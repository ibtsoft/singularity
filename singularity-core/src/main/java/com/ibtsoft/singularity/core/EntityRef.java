package com.ibtsoft.singularity.core;

import java.util.UUID;

public class EntityRef<T> extends Entity<T> {

    private final Repository<T> repository;

    public EntityRef(UUID id, String entityClass, Repository<T> repository) {
        super(id, entityClass);
        this.repository = repository;
    }

    @Override
    public T getValue() {
        return repository.findById(getId()).getValue();
    }
}
