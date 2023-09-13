package com.ibtsoft.singularity.persistence.mongodb.typehandlers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityBasicProperty;

import static java.lang.String.format;

public class MongoDbTypeHandlers {

    private final Map<String, MongoDbTypeHandler> typeHandlers = new HashMap<>();

    public MongoDbTypeHandler getHandler(final EntityBasicProperty property) {
        return typeHandlers.get(property.getField().getType().getSimpleName());
    }

    public MongoDbTypeHandler getHandler(final Class<?> valueClass) {
        MongoDbTypeHandler handler = null;
        Class<?> currentClass = valueClass;
        while (handler == null && currentClass != null) {
            handler = typeHandlers.get(currentClass.getSimpleName());
            currentClass = currentClass.getSuperclass();
        }
        if (handler == null) {
            throw new RuntimeException(format("Cannot find type handler for class %s", valueClass.getSimpleName()));
        }
        return handler;
    }

    public <T extends MongoDbTypeHandler> void addTypeHandler(final Class<T> typeHandlerClass) {
        T typeHandler;

        try {
            typeHandler = (T) typeHandlerClass.getDeclaredConstructor(this.getClass()).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(format("Failed to create MongoDbTypeHandler of class %s", typeHandlerClass.getSimpleName()), e);
        }

        typeHandler.getSupportedClasses().forEach(s -> typeHandlers.put(s, typeHandler));
    }
}
