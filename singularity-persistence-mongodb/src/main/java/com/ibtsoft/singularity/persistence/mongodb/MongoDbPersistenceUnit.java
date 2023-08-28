package com.ibtsoft.singularity.persistence.mongodb;

import java.net.UnknownHostException;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.persistence.PersistenceUnit;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbBasicTypesHandler;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbEntityTypeHandler;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbTypeHandler;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbTypeHandlers;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDbPersistenceUnit extends PersistenceUnit {

    private final EntityStructureCache entityStructureCache;
    private final MongoDbTypeHandlers typeHandlers;

    private final String uri;
    private final String dbName;

    private MongoClient mongoClient;
    private DB db;

    public MongoDbPersistenceUnit(EntityStructureCache entityStructureCache, String uri, String dbName) {
        this.entityStructureCache = entityStructureCache;
        this.typeHandlers = new MongoDbTypeHandlers();
        this.uri = uri;
        this.dbName = dbName;

        addTypeHandler(MongoDbBasicTypesHandler.class);
        addTypeHandler(MongoDbEntityTypeHandler.class);
    }

    public void connect() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(uri));
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
        db = mongoClient.getDB(dbName);
    }

    @Override
    public void disconnect() {
        mongoClient.close();
    }

    @Override
    public <T> Persistence<T> getPersistence(Class<T> entityClass) {
        return new MongoDbPersistence<T>(db.getCollection(entityClass.getSimpleName()), entityClass, entityStructureCache.getEntityStructure(entityClass),
            typeHandlers);
    }

    public <T extends MongoDbTypeHandler> void addTypeHandler(Class<T> typeHandlerClass) {
        typeHandlers.addTypeHandler(typeHandlerClass);
    }
}
