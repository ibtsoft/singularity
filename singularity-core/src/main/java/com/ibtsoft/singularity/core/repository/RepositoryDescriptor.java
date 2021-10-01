package com.ibtsoft.singularity.core.repository;

public class RepositoryDescriptor {

    private final String repositoryName;

    public static RepositoryDescriptor forName(String repositoryName) {
        return new RepositoryDescriptor(repositoryName);
    }

    protected RepositoryDescriptor(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }


}
