package com.ibtsoft.singularity.core;

import java.util.Objects;
import java.util.UUID;

public class Id {

    private final UUID uuid;

    public static Id generate() {
        return new Id();
    }

    protected Id() {
        this.uuid = UUID.randomUUID();
    }

    protected Id(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Id id = (Id) o;
        return Objects.equals(uuid, id.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
