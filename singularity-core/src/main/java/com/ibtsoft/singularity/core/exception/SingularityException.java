package com.ibtsoft.singularity.core.exception;

public class SingularityException extends RuntimeException {

    private final String internalMessage;

    public SingularityException(String message, String internalMessage) {
        super(message);
        this.internalMessage = internalMessage;
    }

}
