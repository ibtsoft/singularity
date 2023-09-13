package com.ibtsoft.singularity.web.messages;

import java.util.UUID;

import com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum;

public class MessageReply extends Message {

    private final ActionResultStatusEnum status;
    private final String message;

    public MessageReply(final Message.Meta meta, final String module, final String type, final ActionResultStatusEnum status, final String message,
        final Object payload) {
        super(UUID.randomUUID().toString(), module, type + "_RESULT", payload);
        if (meta != null) {
            this.setMeta(new Meta(UUID.randomUUID().toString(), meta.getReferenceId()));
        }
        this.status = status;
        this.message = message;
    }

    public MessageReply(final Message.Meta meta, final String module, final String type, final ActionResultStatusEnum status, final String message) {
        this(meta, module, type, status, message, null);
    }

    public ActionResultStatusEnum getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageReply{");
        sb.append("status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Meta extends Message.Meta {

        private final String replyToReferenceId;

        public Meta(final String id, final String replyToMessageId) {
            super(id);
            this.replyToReferenceId = replyToMessageId;
        }

        public String getReplyToReferenceId() {
            return replyToReferenceId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Meta{");
            sb.append("replyToReferenceId='").append(replyToReferenceId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
