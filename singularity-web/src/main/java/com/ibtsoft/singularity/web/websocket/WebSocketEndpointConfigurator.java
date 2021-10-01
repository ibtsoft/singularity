package com.ibtsoft.singularity.web.websocket;

import com.ibtsoft.singularity.core.ActionsRepository;
import com.ibtsoft.singularity.web.websocket.MainEndpoint;
import com.singularity.security.SecurityManager;

import javax.websocket.server.ServerEndpointConfig;

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