package com.ibtsoft.singularity.core.repository.reflection.entitystructure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityStructure {

    private static Logger LOGGER = LoggerFactory.getLogger(EntityStructure.class);

    private final Map<String, Method> otherMethods = new HashMap<>();

    private final Map<String, EntityBasicProperty> properties = new HashMap<>();

    public EntityStructure(Class<?> entityClass) {
        LOGGER.info("Processing structure of entity class {}", entityClass.getSimpleName());
        Map<String, Method> methods = new HashMap<>();

        for (Method method : entityClass.getMethods()) {
            methods.put(method.getName(), method);
        }

        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String propertyName = String.format("%s%s", fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1));
            Method getter = methods.remove(String.format("get%s", propertyName));
            if (getter == null) {
                LOGGER.warn("Field {} of class {} has no getter.", fieldName, entityClass.getSimpleName());
            }
            Method setter = methods.remove(String.format("set%s", propertyName));
            if (setter == null) {
                LOGGER.warn("Field {} of class {} has no setter.", fieldName, entityClass.getSimpleName());
            }
            properties.put(propertyName, new EntityBasicProperty(field, getter, setter));
        }
        otherMethods.putAll(methods);
    }

    public EntityBasicProperty getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Collection<EntityBasicProperty> getProperties() {
        return properties.values();
    }

    public Method getMethod(String methodName) {
        return otherMethods.get(methodName);
    }
}
