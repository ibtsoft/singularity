package com.singularity.security;

import java.util.UUID;

import com.ibtsoft.singularity.core.repository.entity.Id;

public class UserId extends Id {

    private UserId() {
        super();
    }

    private UserId(UUID uuid) {
        super(uuid);
    }

    public static UserId forUUID(UUID uuid) {
        return new UserId(uuid);
    }
}
