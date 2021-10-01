package com.ibtsoft.singularity.web.socketio;

import java.net.URL;
import java.util.Objects;

import org.eclipse.jetty.http.pathmap.ServletPathSpec;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;

import com.ibtsoft.singularity.core.ActionsRepository;
import com.singularity.security.SecurityManager;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.engineio.server.JettyWebSocketHandler;

public class SocketIoJettyServer {

    private Server server;

    public void start(SecurityManager securityManager, ActionsRepository actionsRepository) throws Exception {
        server = new Server(8080);

        EngineIoServerOptions engineIoServerOptions = EngineIoServerOptions.newFromDefault();
        engineIoServerOptions.setHandshakeInterceptor((query, headers) -> {
            if (query.getOrDefault("username","").equals("abc")) {
                return true;
            } else {
                return true;
            }});
            EngineIoServer engineIoServer = new EngineIoServer(engineIoServerOptions);

            ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/");
            servletContextHandler.setAttribute("engineIoServer", engineIoServer);
            servletContextHandler.setAttribute("securityManager", securityManager);
            servletContextHandler.setAttribute("actionsRepository", actionsRepository);
            servletContextHandler.addServlet(SocketIoServlet.class, "/");

            WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(servletContextHandler);
            webSocketUpgradeFilter.addMapping(new ServletPathSpec("/socket.io/*"),
                (servletUpgradeRequest, servletUpgradeResponse) -> new JettyWebSocketHandler(engineIoServer));

            // Add default servlet (to serve the html/css/js)
            // Figure out where the static files are stored.
            URL urlStatics = Thread.currentThread().getContextClassLoader().getResource("index.html");
            Objects.requireNonNull(urlStatics, "Unable to find index.html in classpath");
            String urlBase = urlStatics.toExternalForm().replaceFirst("/[^/]*$", "/");
            ServletHolder defHolder = new ServletHolder("default", new DefaultServlet());
            defHolder.setInitParameter("resourceBase", urlBase);
            defHolder.setInitParameter("dirAllowed", "true");
            servletContextHandler.addServlet(defHolder, "/stat");

            server.start();
            server.join();
        }

        void stop () throws Exception {
            server.stop();
        }
    }
