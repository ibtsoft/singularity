package com.ibtsoft.singularity.core.repository.exception;

import com.ibtsoft.singularity.core.exception.SingularityException;

public class RepositoryException extends SingularityException {

    private final String repositoryName;

    public RepositoryException(final String message, final String internalMessage, final String repositoryName) {
        super(message, internalMessage);
        this.repositoryName = repositoryName;
    }
}
