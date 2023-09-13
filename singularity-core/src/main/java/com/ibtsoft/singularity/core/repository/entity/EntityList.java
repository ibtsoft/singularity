package com.ibtsoft.singularity.core.repository.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityList<T> {

    private final Entity<?> parentEntity;
    private final List<T> list = new CopyOnWriteArrayList<>();

    public EntityList(final Entity<?> parentEntity) {
        this.parentEntity = parentEntity;
    }

    public boolean add(final T item) {
        return list.add(item);
    }

    public T get(final int index) {
        return list.get(index);
    }

    public boolean remove(final T item) {
        return list.remove(item);
    }
}
