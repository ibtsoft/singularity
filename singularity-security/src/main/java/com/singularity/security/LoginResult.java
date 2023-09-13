package com.singularity.security;

import com.singularity.security.token.Token;

public final class LoginResult {

    private final boolean success;
    private final String username;
    private final UserId userId;
    private final Token token;

    private LoginResult(final boolean success, final String username, final UserId userId, final Token token) {
        this.success = success;
        this.username = username;
        this.userId = userId;
        this.token = token;
    }

    public static LoginResult success(final String username, final UserId userId, final Token token) {
        return new LoginResult(true, username, userId, token);
    }

    public static LoginResult wrongUsername(final String username) {
        return new LoginResult(false, username, null, null);
    }

    public static LoginResult wrongPassword(final String username) {
        return new LoginResult(false, username, null, null);
    }

    public static LoginResult wrongToken() {
        return new LoginResult(false, null, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public UserId getUserId() {
        return userId;
    }

    public Token getToken() {
        return token;
    }
}
