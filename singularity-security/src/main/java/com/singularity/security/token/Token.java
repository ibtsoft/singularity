package com.singularity.security.token;

import java.time.LocalDateTime;

public class Token {

    private final String username;
    private final String token;
    private final LocalDateTime expiration;

    public Token(final String username, final String token, final LocalDateTime expiration) {
        this.username = username;
        this.token = token;
        this.expiration = expiration;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
}
