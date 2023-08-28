package com.ibtsoft.singularity.web.socketio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.ibtsoft.singularity.core.action.ActionsRepository;
import com.ibtsoft.singularity.web.Session;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageReply;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.singularity.security.SecurityManager;

import io.socket.socketio.server.SocketIoSocket;

import static com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum.FAILURE;
import static java.lang.String.format;

public class SocketIoSession extends Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIoSession.class);

    private final SocketIoSocket socket;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();

    public SocketIoSession(SocketIoSocket socket, SecurityManager securityManager, ActionsRepository actionsRepository) {
        super(securityManager, actionsRepository);
        this.socket = socket;

        modules.forEach((name, module) -> {
            socket.on(name, args -> {
                LOGGER.info("Received message for module {}: {}", name, args[0]);
                Message message;
                try {
                    message = gson.fromJson((String) args[0], Message.class);
                } catch (JsonSyntaxException e) {
                    LOGGER.error("Failed to parse message ", e);
                    sendMessage(new MessageReply(null, name, null, FAILURE, "Cannot parse the message"));
                    return;
                }
                try {
                    module.processMessage(message);
                } catch (Exception e) {
                    LOGGER.error("Failed to process message ", e);
                    sendMessage(new MessageReply(message.getMeta(), name, message.getType(), FAILURE, format("Failed to process message: %s", e.getMessage())));
                }
            });
        });

        socket.on("error", args -> {
            LOGGER.error("Session error: {}", args[0]);
        });
    }

    @Override
    public void sendMessage(Message message) {
        try {
            socket.send(message.getModule(), gson.toJson(message));
            LOGGER.info("Sent message: {}", message);
        } catch (Exception e) {
            LOGGER.error(format("Failed to send message: %s", message), e);
        }
    }
}
