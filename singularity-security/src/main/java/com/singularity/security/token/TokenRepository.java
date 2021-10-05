package com.singularity.security.token;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.ibtsoft.singularity.core.Entity;
import com.ibtsoft.singularity.core.EntityValue;
import com.ibtsoft.singularity.core.Id;
import com.ibtsoft.singularity.core.Repository;
import com.singularity.security.User;

public class TokenRepository extends Repository<Token> {

    public TokenRepository() {
        super(Token.class);
    }

    public Optional<EntityValue<Token>> findByTokenValue(String tokenValue) {
        return findAll().stream().filter(token-> token.getValue().getToken().equals(tokenValue)).findFirst();
    }

    public Token generateToken(Entity<User> user) {
        return save(new Token(user, UUID.randomUUID().toString(), LocalDateTime.now().plusDays(7))).getValue();
    }

    public Token generateToken(Entity<User> user, UUID oldTokenId) {
        deleteById(oldTokenId);

        return generateToken(user);
    }
}
