package com.ibtsoft.singularity.core;

import java.util.List;
import java.util.UUID;

public interface Persistence<T> {

    void save(EntityValue<T> value);

    List<EntityValue<T>> loadAll();

    void remove(UUID value);

}
