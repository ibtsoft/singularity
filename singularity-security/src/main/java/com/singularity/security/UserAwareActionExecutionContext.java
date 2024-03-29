package com.singularity.security;

import com.ibtsoft.singularity.core.action.ActionExecutionContext;

public class UserAwareActionExecutionContext extends ActionExecutionContext {

    private final UserId userId;

    public UserAwareActionExecutionContext(final UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
