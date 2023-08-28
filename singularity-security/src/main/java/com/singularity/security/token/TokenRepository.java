package com.singularity.security.token;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class TokenRepository extends Repository<Token> {

    public TokenRepository(EntityStructureCache entityStructureCache, TransactionManager transactionManager) {
        super(Token.class, entityStructureCache, transactionManager);
    }

    public Optional<EntityValue<Token>> findByTokenValue(String tokenValue) {
        return findAll().stream().filter(token -> token.getValue().getToken().equals(tokenValue)).findFirst();
    }

    public Token generateToken(String username) {
        return save(new Token(username, UUID.randomUUID().toString(), LocalDateTime.now().plusDays(7))).getValue();
    }

    public Token generateToken(String username, UUID oldTokenId) {
        deleteById(oldTokenId);

        return generateToken(username);
    }
}
