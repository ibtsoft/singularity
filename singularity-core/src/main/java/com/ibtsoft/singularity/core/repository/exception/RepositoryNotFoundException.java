package com.ibtsoft.singularity.core.repository.exception;

public class RepositoryNotFoundException extends RepositoryException {

    public RepositoryNotFoundException(final String message, final String internalMessage, final String repositoryName) {
        super(message, internalMessage, repositoryName);
    }
}
