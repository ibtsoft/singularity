package com.ibtsoft.singularity.web.modules;

import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageSender;

public abstract class Module {

    private final MessageSender messageSender;

    public Module(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public abstract String getName();

    public abstract void processMessage(Message message);

    protected void sendMessage(Message message) {
        messageSender.sendMessage(message);
    }
}
