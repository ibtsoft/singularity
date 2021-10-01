package com.singularity.security;

import java.util.Objects;

public class RepositoryUsername {

    private final String repository;
    private final UserId username;

    public RepositoryUsername(String repository, UserId username) {
        this.repository = repository;
        this.username = username;
    }

    public String getRepository() {
        return repository;
    }

    public UserId getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RepositoryUsername that = (RepositoryUsername) o;
        return Objects.equals(repository, that.repository) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository, username);
    }
}
