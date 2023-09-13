package com.ibtsoft.singularity.core.repository.reflection.entitystructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibtsoft.singularity.core.SingularityConfiguration;

public class EntityStructureCache {

    private final Map<String, EntityStructure> cache = new HashMap<>();

    public EntityStructureCache(final List<SingularityConfiguration.EntityTypeConfiguration> entityTypes) {
        entityTypes.forEach(entityTypeConfiguration ->
            cache.put(
                entityTypeConfiguration.getEntityType().getSimpleName(),
                new EntityStructure(entityTypeConfiguration.getEntityType())));
    }

    public EntityStructure getEntityStructure(final Class<?> entityClass) {
        return cache.get(entityClass.getSimpleName());
    }
}
