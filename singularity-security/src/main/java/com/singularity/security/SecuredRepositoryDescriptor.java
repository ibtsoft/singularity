package com.singularity.security;

import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;

public final class SecuredRepositoryDescriptor extends RepositoryDescriptor {

    private final UserId userId;

    public static SecuredRepositoryDescriptor forClassAndUserId(final Class<?> repositoryEntityClass, final UserId userId) {
        return new SecuredRepositoryDescriptor(repositoryEntityClass.getSimpleName(), userId);
    }

    private SecuredRepositoryDescriptor(final String repositoryName, final UserId userId) {
        super(repositoryName);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
