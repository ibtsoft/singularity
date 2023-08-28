package com.ibtsoft.singularity.core.persistence;

import java.util.List;
import java.util.UUID;

import com.ibtsoft.singularity.core.repository.entity.EntityValue;

public interface Persistence<T> {

    void save(EntityValue<T> value);

    List<EntityValue<T>> loadAll();

    void remove(UUID value);
}
