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

import com.ibtsoft.singularity.core.ActionsRepository;
import com.singularity.security.SecurityManager;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;

@WebServlet("/socket.io/*")
public class SocketIoServlet extends HttpServlet {

    private EngineIoServer mEngineIoServer;

    private final List<SocketSession> sessions = new CopyOnWriteArrayList<>();

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

            /*JSONObject jsonObject = (JSONObject) socket.getConnectData();
            if (jsonObject.has("username")) {
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                LoginResult loginResult = securityManager.login(username, password);
                if (loginResult.isSuccess()) {
                    sessions.add(new SocketSession(socket, securityManager, actionsRepository, loginResult));
                } else               {
                    socket.send("connection_error", "Error");
                }
            } else {*/
            sessions.add(new SocketSession(socket, securityManager, actionsRepository));
            /*}
             */
        });

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mEngineIoServer.handleRequest(request, response);
    }
}
