package com.ibtsoft.singularity.web.modules.authentication.messages;

public class LoginMessage {

    private final String username;
    private final String password;

    public LoginMessage(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
