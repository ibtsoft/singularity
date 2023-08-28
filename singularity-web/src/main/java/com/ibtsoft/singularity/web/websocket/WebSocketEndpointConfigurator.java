package com.ibtsoft.singularity.web.websocket;

import javax.websocket.server.ServerEndpointConfig;

import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.singularity.security.SecurityManager;

public class WebSocketEndpointConfigurator extends ServerEndpointConfig.Configurator {

    private final SecurityManager securityManager;
    private final ActionsRepository actionsRepository;

    public WebSocketEndpointConfigurator(SecurityManager securityManager, ActionsRepository actionsRepository) {
        this.securityManager = securityManager;
        this.actionsRepository = actionsRepository;
    }

    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return (T) new MainEndpoint(securityManager, actionsRepository);
    }
}
