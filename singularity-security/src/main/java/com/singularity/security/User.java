package com.singularity.security;

public class User {

    private final String username;

    private String password;

    public User(final String username) {
        this.username = username;
    }

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
