package com.singularity.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ibtsoft.singularity.core.EntityValue;
import com.ibtsoft.singularity.core.Persistence;
import com.ibtsoft.singularity.core.Repository;

import static java.util.Collections.emptyMap;

public class AclRulesRepository extends Repository<AclRule> {

    private final Map<RepositoryUsername, Map<UUID, AclRule>> aclRulesByRepositoryAndUsername = new ConcurrentHashMap<>();

    public AclRulesRepository(Class<AclRule> repositoryClass) {
        super(repositoryClass);
    }

    public AclRulesRepository(Class<AclRule> repositoryClass, Persistence<AclRule> persistence) {
        super(repositoryClass, persistence);
    }

    @Override
    public EntityValue<AclRule> save(AclRule item) {
        EntityValue<AclRule> aclRule = super.save(item);
        aclRulesByRepositoryAndUsername
            .computeIfAbsent(new RepositoryUsername(item.getEntity().getEntityClass(), item.getUserId()), repositoryUsername -> new ConcurrentHashMap<>())
            .computeIfAbsent(item.getEntity().getId(), uuid -> item);
        return aclRule;
    }

    public Map<UUID, AclRule> findByUserIdAndRepository(UserId userId, String repositoryName) {
        return aclRulesByRepositoryAndUsername.getOrDefault(new RepositoryUsername(repositoryName, userId), emptyMap());
    }
}
