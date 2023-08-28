package com.ibtsoft.singularity.core.repository.exception;

import com.ibtsoft.singularity.core.repository.entity.Id;

import static java.lang.String.format;

public class EntityNotFoundException extends RepositoryException {

    private final Id entityId;

    public EntityNotFoundException(String repositoryName, Id entityId) {
        super(makeMessage(repositoryName, entityId), makeMessage(repositoryName, entityId), repositoryName);
        this.entityId = entityId;
    }

    private static String makeMessage(String repositoryName, Id entityId) {
        return format("Entity not found, repositoryName=%s, id=%s,", repositoryName, entityId);
    }

    public Id getEntityId() {
        return entityId;
    }
}
