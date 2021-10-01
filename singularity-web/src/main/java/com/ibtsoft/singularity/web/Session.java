package com.ibtsoft.singularity.web;

import java.util.HashMap;
import java.util.Map;

import com.ibtsoft.singularity.core.ActionsRepository;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ActionModule;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationModule;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationResultListener;
import com.ibtsoft.singularity.web.modules.repository.RepositoryModule;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

public abstract class Session implements MessageSender, AuthenticationResultListener {

    protected Map<String, Module> modules = new HashMap<>();

    private String username;
    private UserId userId;


    public Session(SecurityManager securityManager, ActionsRepository actionsRepository) {
        AuthenticationModule authenticationModule = new AuthenticationModule(this,securityManager);
        authenticationModule.addAuthenticationResultListener(this);
        modules.put(authenticationModule.getName(), authenticationModule);

        RepositoryModule repositoryModule = new RepositoryModule(this, securityManager);
        authenticationModule.addAuthenticationResultListener(repositoryModule);
        modules.put(repositoryModule.getName(), repositoryModule);

        ActionModule actionModule = new ActionModule(this, actionsRepository);
        modules.put(actionModule.getName(), actionModule);
    }

    @Override
    public void onAuthenticationSuccess(String username, UserId userId) {
        this.username = username;
        this.userId = userId;
    }
}
