package com.ibtsoft.singularity.core.repository;

public class RepositoryDescriptor {

    private final String repositoryName;

    public static RepositoryDescriptor forClass(final Class<?> repositoryEntityClass) {
        return new RepositoryDescriptor(repositoryEntityClass.getSimpleName());
    }

    public static RepositoryDescriptor forName(final String repositoryName) {
        return new RepositoryDescriptor(repositoryName);
    }

    protected RepositoryDescriptor(final String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }
}
