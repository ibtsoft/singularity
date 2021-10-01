package com.ibtsoft.singularity.persistance.mongodb;

import java.net.UnknownHostException;

import com.ibtsoft.singularity.core.EntityValue;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDbPersistenceUnit implements  Pers

    private final String uri;
    private final String dbName;
    private MongoClient mongoClient;
    private DB db;

    public MongoDbPersistenceUnit(String uri, String dbName) {
        this.uri = uri;
        this.dbName = dbName;
    }

    protected void connect() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(uri));
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
        db = mongoClient.getDB(dbName);
    }

    public void  save(EntityValue<?> entity) {
        DBCollection collection = db.getCollection(entity.getValue().getClass().getSimpleName());
        collection.insert(new BasicDBObject().append("_id", entity.getId()));
    }
}
