package com.singularity.security.excepiton;

import com.ibtsoft.singularity.core.exception.SingularityException;

public class UserNotFoundException extends SingularityException {

    private final String username;

    public UserNotFoundException(String username) {
        super(makeMessage(username), makeMessage(username));
        this.username = username;
    }

    private static String makeMessage(String username) {
        return String.format("User with username '%s' is not found", username);
    }

    public String getUsername() {
        return username;
    }
}
