package com.singularity.security;

public class User {

    private final String username;

    private  String password;

    public User(String username) {this.username = username;}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}