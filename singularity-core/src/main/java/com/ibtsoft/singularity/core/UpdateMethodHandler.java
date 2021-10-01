package com.ibtsoft.singularity.core;

import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.util.proxy.MethodHandler;

public class UpdateMethodHandler implements MethodHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMethodHandler.class);

    private final UUID id;
    private final Repository<?> repository;

    public UpdateMethodHandler(Repository<?> repository, UUID id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {
        LOGGER.debug("Class: {}, Id: {}, Method name: {}, Arguments: {}" , self.getClass().getSimpleName(), id, m.getName(), args);
        return proceed.invoke(self, args);  // execute the original method.
    }
}
