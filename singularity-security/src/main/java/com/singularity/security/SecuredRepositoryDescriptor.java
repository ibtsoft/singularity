package com.singularity.security;

import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;

public class SecuredRepositoryDescriptor extends RepositoryDescriptor {

    private final UserId userId;

    public static SecuredRepositoryDescriptor forClassAndUserId(Class<?> repositoryEntityClass, UserId userId) {
        return new SecuredRepositoryDescriptor(repositoryEntityClass.getSimpleName(), userId);
    }

    private SecuredRepositoryDescriptor(String repositoryName, UserId userId) {
        super(repositoryName);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
