package com.ibtsoft.singularity.web.messages;

import java.util.UUID;

import com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum;

public class MessageReply extends Message {

    private final String replyToMessageId;
    private final ActionResultStatusEnum status;
    private final String message;

    public MessageReply(String replyToMessageId, String module, String type, ActionResultStatusEnum status, String message) {
        super(UUID.randomUUID().toString(), module, type+"_RESULT");
        this.replyToMessageId = replyToMessageId;
        this.status = status;
        this.message = message;
    }

    public String getReplyToMessageId() {
        return replyToMessageId;
    }

    public ActionResultStatusEnum getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
