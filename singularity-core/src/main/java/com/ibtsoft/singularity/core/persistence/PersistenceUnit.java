package com.ibtsoft.singularity.core.persistence;

public abstract class PersistenceUnit {

    public abstract void connect();

    public abstract void disconnect();

    public abstract <T> Persistence<T> getPersistence(Class<T> modelClass);
}
