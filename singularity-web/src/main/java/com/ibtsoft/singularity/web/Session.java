package com.ibtsoft.singularity.web;

import java.util.HashMap;
import java.util.Map;

import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ActionModule;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationModule;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationResultListener;
import com.ibtsoft.singularity.web.modules.repository.RepositoryModule;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

public abstract class Session implements MessageSender, AuthenticationResultListener {

    @SuppressWarnings("checkstyle:VisibilityModifier")
    protected Map<String, Module> modules = new HashMap<>();

    private String username;
    private UserId userId;

    private final AuthenticationModule authenticationModule;
    private final RepositoryModule repositoryModule;
    private final ActionModule actionModule;

    public Session(final SecurityManager securityManager, final ActionsRepository actionsRepository) {
        authenticationModule = new AuthenticationModule(this, securityManager);
        authenticationModule.addAuthenticationResultListener(this);
        modules.put(authenticationModule.getName(), authenticationModule);

        repositoryModule = new RepositoryModule(this, securityManager);
        authenticationModule.addAuthenticationResultListener(repositoryModule);
        modules.put(repositoryModule.getName(), repositoryModule);

        actionModule = new ActionModule(this, actionsRepository);
        authenticationModule.addAuthenticationResultListener(actionModule);
        modules.put(actionModule.getName(), actionModule);
    }

    @Override
    public void onAuthenticationSuccess(final String username, final UserId userId) {
        this.username = username;
        this.userId = userId;
    }

    public void close() {
        authenticationModule.removeAuthenticationResultListener(this);
        authenticationModule.removeAuthenticationResultListener(repositoryModule);
        authenticationModule.removeAuthenticationResultListener(actionModule);
    }
}
