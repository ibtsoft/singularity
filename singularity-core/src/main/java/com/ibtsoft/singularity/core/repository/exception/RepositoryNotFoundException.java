package com.ibtsoft.singularity.core.repository.exception;

public class RepositoryNotFoundException extends RepositoryException {

    public RepositoryNotFoundException(String message, String internalMessage, String repositoryName) {
        super(message, internalMessage, repositoryName);
    }
}
