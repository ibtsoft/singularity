package com.singularity.security.token;

import java.time.LocalDateTime;

import com.ibtsoft.singularity.core.Entity;
import com.singularity.security.User;

public class Token {

    private final Entity<User> user;
    private final String token;
    private final LocalDateTime expiration;

    public Token(Entity<User> user, String token, LocalDateTime expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public Entity<User> getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
}
