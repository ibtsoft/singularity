package com.ibtsoft.singularity.web.modules.repository.messages;

import java.util.HashMap;

import com.ibtsoft.singularity.web.messages.Message;

public class RepositoryCrudReplyMessage extends Message {

    public RepositoryCrudReplyMessage(String id, String type, HashMap<String, Object> data) {
        super(id, "REPOSITORY", type, data);
    }

    public enum RepositoryCrudMessageActionEnum {
        SUBSCRIBE, CREATE, UPDATE, DELETE
    }
}
