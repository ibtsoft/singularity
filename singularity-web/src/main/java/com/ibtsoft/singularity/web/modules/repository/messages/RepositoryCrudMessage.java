package com.ibtsoft.singularity.web.modules.repository.messages;

import java.util.List;

public class RepositoryCrudMessage {

    private String repository;

    private List<Object> entities;

    public String getRepository() {
        return repository;
    }

    public void setRepository(final String repository) {
        this.repository = repository;
    }

    public List<Object> getEntities() {
        return entities;
    }

    public void setEntities(final List<Object> entities) {
        this.entities = entities;
    }
}
