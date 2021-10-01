package com.ibtsoft.singularity.web.websocket;

import com.ibtsoft.singularity.core.ActionsRepository;
import com.singularity.security.SecurityManager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import java.net.URL;
import java.util.Objects;

public class WebSocketJettyServer {

    private Server server;

    public void start(SecurityManager securityManager, ActionsRepository actionsRepository) throws Exception {

        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

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
