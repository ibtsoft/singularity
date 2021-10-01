package com.ibtsoft.singularity.web.websocket;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.core.ActionsRepository;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

@ServerEndpoint(value = "/main", decoders = { MessageDecoder.class }, encoders = { MessageEncoder.class })
public class MainEndpoint extends com.ibtsoft.singularity.web.Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainEndpoint.class);

    private Session session;

    private static Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();

    private static HashMap<String, String> users = new HashMap<>();

    public MainEndpoint(SecurityManager securityManager, ActionsRepository actionsRepository) {
        super(securityManager, actionsRepository);
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        users.put(session.getId(), "");
        LOGGER.info("Session is opened, session={}", session);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        Module module = modules.get(message.getModule());
        if (module != null) {
            module.processMessage(message);
        } else {
            LOGGER.error("Unknown module name, {}", message.getModule());
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        LOGGER.info("Session is closed, session={}", session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.error("Session error", throwable);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            session.getBasicRemote().sendObject(gson.toJson(message));
        } catch (IOException | EncodeException e) {
            LOGGER.error("Failed to send message, {}", message);
        }
    }
}
