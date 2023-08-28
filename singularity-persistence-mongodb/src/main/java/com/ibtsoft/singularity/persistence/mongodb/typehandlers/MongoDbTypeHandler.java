package com.ibtsoft.singularity.persistence.mongodb.typehandlers;

import java.util.Set;

import com.mongodb.BasicDBObject;

public abstract class MongoDbTypeHandler {

    private final Set<String> supportedClasses;
    private final MongoDbTypeHandlers typeHandlers;

    public MongoDbTypeHandler(MongoDbTypeHandlers typeHandlers) {
        this.typeHandlers = typeHandlers;
        this.supportedClasses = defineSupportedClasses();
    }

    protected abstract Set<String> defineSupportedClasses();

    public Set<String> getSupportedClasses() {
        return supportedClasses;
    }

    public abstract BasicDBObject toBasicDBObject(Object value);

    public abstract Object toObject(BasicDBObject value);

    public BasicDBObject handleOtherType(Object value) {
        return typeHandlers.getHandler(value.getClass()).toBasicDBObject(value);
    }
}
