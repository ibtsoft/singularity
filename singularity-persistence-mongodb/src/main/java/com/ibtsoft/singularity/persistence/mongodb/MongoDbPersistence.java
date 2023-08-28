package com.ibtsoft.singularity.persistence.mongodb;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructure;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbTypeHandler;
import com.ibtsoft.singularity.persistence.mongodb.typehandlers.MongoDbTypeHandlers;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import static java.lang.String.format;

public class MongoDbPersistence<T> implements Persistence<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbPersistence.class);

    private static final String FIELD_FULL_ID = "_fullId";

    private final DBCollection collection;
    private final Class<T> entityClass;
    private final EntityStructure entityStructure;
    private final MongoDbTypeHandlers typeHandlers;

    public MongoDbPersistence(DBCollection collection, Class<T> entityClass, EntityStructure entityStructure, MongoDbTypeHandlers typeHandlers) {
        this.collection = collection;
        this.entityClass = entityClass;
        this.entityStructure = entityStructure;
        this.typeHandlers = typeHandlers;
    }

    @Override
    public void save(EntityValue<T> value) {
        ObjectId objectId = new ObjectId(value.getId().toString().substring(9).replace("-", ""));

        LOGGER.info("Saving entity type {} with id {} (ObjectId {})", value.getEntityClass(), value.getId(), objectId);

        BasicDBObject dbObject = new BasicDBObject("_id", objectId);
        dbObject.append(FIELD_FULL_ID, value.getId().toString());
        dbObject.append("_type", value.getEntityClass());

        entityStructure.getProperties().forEach(property -> {
            MongoDbTypeHandler mongoDbTypeHandler = typeHandlers.getHandler(property);

            try {
                Object object = property.getGetter().invoke(value.getValue());

                BasicDBObject dbObjectEntity = mongoDbTypeHandler.toBasicDBObject(object);

                dbObject.append(property.getField().getName(), dbObjectEntity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(
                    format("Failed to save entity of class %s, property %s, id %s", entityClass.getName(), property.getField().getName(), value.getId()), e);
            }
        });
        collection.insert(dbObject);
    }

    @Override
    public List<EntityValue<T>> loadAll() {
        DBCursor cursor = collection.find();

        List<EntityValue<T>> result = new ArrayList<>();

        while (cursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject) cursor.next();

            T entity;

            try {
                entity = (T) entityClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(format("Failed to load entity of class %s", entityClass.getName()), e);
            }

            entityStructure.getProperties().forEach(property -> {
                BasicDBObject basicDBObject = (BasicDBObject) dbObject.get(property.getField().getName());

                MongoDbTypeHandler mongoDbTypeHandler = typeHandlers.getHandler(property);

                Object value = mongoDbTypeHandler.toObject(basicDBObject);

                try {
                    property.getSetter().invoke(entity, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(format("Failed to load entity of class %s", entityClass.getName()), e);
                }
            });

            result.add(new EntityValue<T>(UUID.fromString(dbObject.getString(FIELD_FULL_ID)), entityClass.getSimpleName(), entity));
        }
        return result;
    }

    @Override
    public void remove(UUID value) {
    }
}
