package com.ibtsoft.singularity.web.modules;

import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.messages.Message;

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
