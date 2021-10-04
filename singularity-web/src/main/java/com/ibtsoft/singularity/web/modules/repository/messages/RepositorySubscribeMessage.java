package com.ibtsoft.singularity.web.modules.repository.messages;

import java.util.List;

public class RepositorySubscribeMessage {

    private List<String> repositories;

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }
}
