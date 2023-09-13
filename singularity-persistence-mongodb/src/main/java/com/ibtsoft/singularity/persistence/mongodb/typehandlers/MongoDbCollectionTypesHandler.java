package com.ibtsoft.singularity.persistence.mongodb.typehandlers;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.mongodb.BasicDBObject;

public class MongoDbCollectionTypesHandler extends MongoDbTypeHandler {

    public MongoDbCollectionTypesHandler(final MongoDbTypeHandlers typeHandlers) {
        super(typeHandlers);
    }

    @Override
    protected Set<String> defineSupportedClasses() {
        return ImmutableSet.of(List.class.getSimpleName());
    }

    @Override
    public BasicDBObject toBasicDBObject(final Object value) {

        return null;
    }

    @Override
    public Object toObject(final BasicDBObject value) {
        return null;
    }
}
