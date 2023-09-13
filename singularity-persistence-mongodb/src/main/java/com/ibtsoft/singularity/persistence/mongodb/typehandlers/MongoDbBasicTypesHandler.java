package com.ibtsoft.singularity.persistence.mongodb.typehandlers;

import java.math.BigDecimal;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.mongodb.BasicDBObject;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

public class MongoDbBasicTypesHandler extends MongoDbTypeHandler {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_VALUE = "value";

    private static final String TYPE_INT = "int";
    private static final String TYPE_INTEGER = "Integer";
    private static final String TYPE_STRING = "String";
    private static final String TYPE_BIG_DECIMAL = "BigDecimal";

    public MongoDbBasicTypesHandler(final MongoDbTypeHandlers typeHandlers) {
        super(typeHandlers);
    }

    @Override
    protected Set<String> defineSupportedClasses() {
        return ImmutableSet.of(int.class, Integer.class, String.class, BigDecimal.class).stream().map(Class::getSimpleName).collect(toSet());
    }

    @Override
    public BasicDBObject toBasicDBObject(final Object value) {
        assert getSupportedClasses().contains(value.getClass().getSimpleName());

        String type = value.getClass().getSimpleName();

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append(FIELD_TYPE, type);
        switch (type) {
            case TYPE_INT:
            case TYPE_INTEGER:
            case TYPE_STRING:
                basicDBObject.append(FIELD_VALUE, value);
                break;
            case TYPE_BIG_DECIMAL:
                basicDBObject.append(FIELD_VALUE, value.toString());
                break;
            default:
                throw new RuntimeException(format("Unknown type %s", type));
        }

        return basicDBObject;
    }

    @Override
    public Object toObject(final BasicDBObject value) {
        assert getSupportedClasses().contains(value.getString(FIELD_TYPE));

        Object result;

        String type = value.getString(FIELD_TYPE);
        switch (type) {
            case TYPE_INT:
            case TYPE_INTEGER:
                result = value.getInt(FIELD_VALUE);
                break;
            case TYPE_STRING:
                result = value.getString(FIELD_VALUE);
                break;
            case TYPE_BIG_DECIMAL:
                result = new BigDecimal(value.getString(FIELD_VALUE));
                break;
            default:
                throw new RuntimeException(format("Unknown type %s", type));
        }

        return result;
    }
}
