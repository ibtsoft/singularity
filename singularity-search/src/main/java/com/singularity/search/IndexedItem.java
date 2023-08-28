package com.singularity.search;

import java.util.List;
import java.util.Map;

import com.ibtsoft.singularity.core.repository.entity.EntityRef;

public class IndexedItem {

    private String item;

    private Map<EntityType, List<EntityRef<?>>> entities;

    public String getItem() {
        return item;
    }

    public Map<EntityType, List<EntityRef<?>>> getEntities() {
        return entities;
    }
}
