package com.singularity.security;

import com.ibtsoft.singularity.core.repository.entity.EntityRef;

public class AclRule {

    private final UserId userId;
    private final EntityRef<?> entity;
    private final boolean isCreator;
    private final boolean canView;
    private final boolean canChange;

    public AclRule(final UserId userId, final EntityRef<?> entity, final boolean isCreator, final boolean canView, final boolean canChange) {
        this.userId = userId;
        this.entity = entity;
        this.isCreator = isCreator;
        this.canView = canView;
        this.canChange = canChange;
    }

    public UserId getUserId() {
        return userId;
    }

    public EntityRef<?> getEntity() {
        return entity;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public boolean isCanView() {
        return canView;
    }

    public boolean isCanChange() {
        return canChange;
    }
}
