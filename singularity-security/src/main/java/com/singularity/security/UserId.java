package com.singularity.security;

import java.util.UUID;

import com.ibtsoft.singularity.core.repository.entity.Id;

public final class UserId extends Id {

    private UserId() {
        super();
    }

    private UserId(final UUID uuid) {
        super(uuid);
    }

    public static UserId forUUID(final UUID uuid) {
        return new UserId(uuid);
    }
}
