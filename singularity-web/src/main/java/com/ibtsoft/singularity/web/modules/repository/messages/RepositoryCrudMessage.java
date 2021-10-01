package com.ibtsoft.singularity.web.modules.repository.messages;

import com.ibtsoft.singularity.web.messages.Message;

public class RepositoryCrudMessage extends Message {
    private String repository;

    public RepositoryCrudMessage() {
    }

    public RepositoryCrudMessage(String id, String type, Object data, String repository) {
        super(id, "REPOSITORY", type, data);
        this.repository = repository;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public enum RepositoryCrudMessageActionEnum {
        SUBSCRIBE, CREATE, UPDATE, DELETE
    }
}
