package com.ibtsoft.singularity.core.exception;

public class SingularityException extends RuntimeException {

    private final String internalMessage;

    public SingularityException(final String message, final String internalMessage) {
        super(message);
        this.internalMessage = internalMessage;
    }
}
