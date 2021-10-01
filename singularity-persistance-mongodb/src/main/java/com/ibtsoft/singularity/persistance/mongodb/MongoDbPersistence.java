package com.ibtsoft.singularity.persistance.mongodb;

import java.util.List;
import java.util.UUID;

import com.ibtsoft.singularity.core.EntityValue;
import com.ibtsoft.singularity.core.Persistence;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class MongoDbPersistence<T> implements Persistence<T> {

    private final DBCollection collection;

    public MongoDbPersistence(DBCollection collection) {this.collection = collection;}

    @Override
    public void save(EntityValue<T> value) {

    }

    @Override
    public List<EntityValue<T>> loadAll() {
        DBCursor cursor = collection.find();
        cursor.
        return ;
    }

    @Override
    public void remove(UUID value) {

    }
}
