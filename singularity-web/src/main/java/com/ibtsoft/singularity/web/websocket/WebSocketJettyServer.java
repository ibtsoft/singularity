package com.ibtsoft.singularity.web.websocket;

import java.net.URL;
import java.util.Objects;

import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.singularity.security.SecurityManager;

public class WebSocketJettyServer {

    private Server server;

    @SuppressWarnings("checkstyle:MagicNumber")
    public void start(final SecurityManager securityManager, final ActionsRepository actionsRepository) throws Exception {
        server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add javax.websocket support
        ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
        container.addEndpoint(ServerEndpointConfig.Builder
            .create(MainEndpoint.class, "/") // the endpoint url
            .configurator(new WebSocketEndpointConfigurator(securityManager, actionsRepository))
            .build());

        // Add default servlet (to serve the html/css/js)
        // Figure out where the static files are stored.
        URL urlStatics = Thread.currentThread().getContextClassLoader().getResource("index.html");
        Objects.requireNonNull(urlStatics, "Unable to find index.html in classpath");
        String urlBase = urlStatics.toExternalForm().replaceFirst("/[^/]*$", "/");
        ServletHolder defHolder = new ServletHolder("default", new DefaultServlet());
        defHolder.setInitParameter("resourceBase", urlBase);
        defHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(defHolder, "/");

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
