package com.ibtsoft.singularity.web.socketio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.core.ActionsRepository;
import com.ibtsoft.singularity.web.Session;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.singularity.security.SecurityManager;

import io.socket.socketio.server.SocketIoSocket;

public class SocketSession extends Session {

    private final SocketIoSocket socket;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();

    public SocketSession(SocketIoSocket socket, SecurityManager securityManager, ActionsRepository actionsRepository) {
        super(securityManager, actionsRepository);
        this.socket = socket;

        modules.forEach((name, module) -> {
            socket.on(name, args -> {
                module.processMessage(gson.fromJson((String) args[0], Message.class));
            });
        });
    }

    @Override
    public void sendMessage(Message message) {
        socket.send(message.getModule(), gson.toJson(message));
    }
}