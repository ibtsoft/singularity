package com.ibtsoft.singularity.web.modules.repository.messages;

import java.util.HashMap;

import com.ibtsoft.singularity.web.messages.Message;

public class RepositoryCrudReplyMessage extends Message {

    public RepositoryCrudReplyMessage(final String id, final String type, final HashMap<String, Object> data) {
        super(id, "REPOSITORY", type, data);
    }

    public enum RepositoryCrudMessageActionEnum {
        SUBSCRIBE, UNSUBSCRIBE, CREATE, UPDATE, DELETE
    }
}
