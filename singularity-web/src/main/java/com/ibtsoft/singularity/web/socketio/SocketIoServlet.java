package com.ibtsoft.singularity.web.socketio;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.singularity.security.SecurityManager;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

@WebServlet("/socket.io/*")
public class SocketIoServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIoServlet.class);

    private EngineIoServer mEngineIoServer;

    private final List<SocketIoSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();

        mEngineIoServer = (EngineIoServer) ctx.getAttribute("engineIoServer");

        SocketIoServer mSocketIoServer = new SocketIoServer(mEngineIoServer);

        SecurityManager securityManager = (SecurityManager) ctx.getAttribute("securityManager");

        ActionsRepository actionsRepository = (ActionsRepository) ctx.getAttribute("actionsRepository");

        SocketIoNamespace namespace = mSocketIoServer.namespace("/");

        namespace.on("connection", args -> {
            SocketIoSocket socket = (SocketIoSocket) args[0];

            LOGGER.debug("Client connected, id={}", socket.getId());

            SocketIoSession socketSession = new SocketIoSession(socket, securityManager, actionsRepository);

            sessions.add(socketSession);

            socket.on("disconnect", args1 -> {
                socketSession.close();
                sessions.remove(socketSession);
                LOGGER.debug("Client disconnected, id={}", socket.getId());
            });
        });
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        mEngineIoServer.handleRequest(request, response);
    }
}
