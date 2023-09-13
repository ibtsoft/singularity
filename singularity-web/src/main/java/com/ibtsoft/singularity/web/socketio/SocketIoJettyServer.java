package com.ibtsoft.singularity.web.socketio;

import java.net.URL;
import java.util.Objects;

import org.eclipse.jetty.http.pathmap.ServletPathSpec;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.singularity.security.SecurityManager;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoServerOptions;
import io.socket.engineio.server.JettyWebSocketHandler;

public class SocketIoJettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIoJettyServer.class);

    private Server server;

    @SuppressWarnings("checkstyle:MagicNumber")
    public void start(final SecurityManager securityManager, final ActionsRepository actionsRepository) throws Exception {
        server = new Server(8080);

        Slf4jRequestLogWriter slf4jWriter = new Slf4jRequestLogWriter();

        CustomRequestLog requestLog = new CustomRequestLog(slf4jWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT);

        server.setRequestLog(requestLog);

        EngineIoServerOptions engineIoServerOptions = EngineIoServerOptions.newFromDefault();
        engineIoServerOptions.setHandshakeInterceptor((query, headers) -> {
            return true;
        });
        EngineIoServer engineIoServer = new EngineIoServer(engineIoServerOptions);

        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/");
        servletContextHandler.setAttribute("engineIoServer", engineIoServer);
        servletContextHandler.setAttribute("securityManager", securityManager);
        servletContextHandler.setAttribute("actionsRepository", actionsRepository);
        servletContextHandler.addServlet(SocketIoServlet.class, "/socket.io/*");

        WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(servletContextHandler);
        webSocketUpgradeFilter.addMapping(new ServletPathSpec("/socket.io/*"),
            (servletUpgradeRequest, servletUpgradeResponse) -> new JettyWebSocketHandler(engineIoServer));

        // Add default servlet (to serve the html/css/js)
        // Figure out where the static files are stored.
        URL index = Thread.currentThread().getContextClassLoader().getResource("index.html");
        Objects.requireNonNull(index, "Unable to find index.html in classpath");
        String urlBase = index.toExternalForm().replaceFirst("/[^/]*$", "/");
        ServletHolder defHolder = new ServletHolder("default", new DefaultServlet());
        defHolder.setInitParameter("resourceBase", urlBase);
        defHolder.setInitParameter("dirAllowed", "false");
        servletContextHandler.addServlet(defHolder, "/");

        server.start();
        server.join();
    }

    void stop() throws Exception {
        server.stop();
    }
}
