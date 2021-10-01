package com.singularity.security;

import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;

public class SecuredRepositoryDescriptor extends RepositoryDescriptor {

    private final UserId userId;

    public static SecuredRepositoryDescriptor forNameAndUserId(String repositoryName, UserId userId) {
        return new SecuredRepositoryDescriptor(repositoryName, userId);
    }

    private SecuredRepositoryDescriptor(String repositoryName, UserId userId) {
        super(repositoryName);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
