package com.singularity.security;

public class LoginResult {

    private final boolean success;
    private final String username;
    private final UserId userId;

    private LoginResult(boolean success, String username, UserId userId) {
        this.success = success;
        this.username = username;
        this.userId = userId;
    }

    public static LoginResult success(String username, UserId userId) {
        return new LoginResult(true, username, userId);
    }

    public static LoginResult wrongUsername(String username) {
        return new LoginResult(false, username, null);
    }

    public static LoginResult wrongPassword(String username) {
        return new LoginResult(false, username, null);
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
}
