package com.ibtsoft.singularity.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicInvocationHandler<T> implements InvocationHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DynamicInvocationHandler.class);

  private final Map<String, Method> methods = new HashMap<>();

  private T target;

  public DynamicInvocationHandler(final T target) {
    this.target = target;

    for (Method method : target.getClass().getDeclaredMethods()) {
      this.methods.put(method.getName(), method);
    }
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    LOGGER.debug("Calling {}", method.getName());
    Object result = methods.get(method.getName()).invoke(target, args);
    return result;
  }
}
