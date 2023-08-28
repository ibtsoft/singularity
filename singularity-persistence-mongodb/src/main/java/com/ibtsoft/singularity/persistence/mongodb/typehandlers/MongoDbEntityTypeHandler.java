package com.ibtsoft.singularity.persistence.mongodb.typehandlers;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;
import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.ibtsoft.singularity.core.repository.entity.EntityRef;
import com.mongodb.BasicDBObject;

import static java.util.stream.Collectors.toSet;

public class MongoDbEntityTypeHandler extends MongoDbTypeHandler {

    private static final String FIELD_ID = "id";
    private static final String FIELD_TYPE = "entity";
    private static final String FIELD_CLASS = "class";

    private static final String TYPE = "entity";

    public MongoDbEntityTypeHandler(MongoDbTypeHandlers typeHandlers) {
        super(typeHandlers);
    }

    @Override
    protected Set<String> defineSupportedClasses() {
        return ImmutableSet.of(Entity.class, EntityRef.class).stream().map(Class::getSimpleName).collect(toSet());
    }

    @Override
    public BasicDBObject toBasicDBObject(Object value) {
        assert value instanceof Entity<?>;

        Entity<?> entity = (Entity<?>) value;

        BasicDBObject dbObjectEntity = new BasicDBObject(FIELD_ID, entity.getId());
        dbObjectEntity.append(FIELD_TYPE, TYPE);
        dbObjectEntity.append(FIELD_CLASS, entity.getEntityClass());

        return dbObjectEntity;
    }

    @Override
    public Object toObject(BasicDBObject value) {
        assert TYPE.equals(value.getString(FIELD_TYPE));

        UUID id = UUID.fromString(value.getString(FIELD_ID));
        String entityClass = value.getString(FIELD_CLASS);

        return new EntityRef<>(id, entityClass);
    }
}
