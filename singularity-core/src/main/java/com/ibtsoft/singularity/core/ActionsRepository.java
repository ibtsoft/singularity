package com.ibtsoft.singularity.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class ActionsRepository {

    private Map<String, ActionMethod> actions = new ConcurrentHashMap<>();

    private static final ActionsRepository instance = new ActionsRepository();

    private ActionsRepository() {
    }

    public static ActionsRepository actionsRepository() {
        return instance;
    }

    public void addActions(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(Action.class)) {
                String actionName = method.getName();
                if (actions.containsKey(actionName)) {
                    throw new RuntimeException(format("Action with name '%s' already exists", actionName));
                } else {
                    actions.put(actionName, new ActionMethod(object, method));
                }
            }
        }
    }

    public void executeAction(String actionName, Map<String, Object> params) {
        ActionMethod actionMethod = actions.get(actionName);
        if (actionMethod == null) {
            throw new RuntimeException(format("Cannot find action '%s'", actionName));
        }
        List<Object> args = new ArrayList<>();
/*        for (Parameter parameter : actionMethod.getMethod().getParameters()) {
            if (parameter.isNamePresent()) {
                Object param = params.get(parameter.getName());
                if (param == null) {
                    throw new RuntimeException(format("Unknown parameter name '%s' in action '%s'", parameter.getName(), actionName));
                }
                args.add(params.get(parameter.getName()));
            } else {
                args.add(params.entrySet().)
            }

        }

 */
        try {
            actionMethod.getMethod().invoke(actionMethod.getObject(), params.values().toArray());
        } catch (Exception e) {
            throw new RuntimeException(format("Exception occurred when called action '%s'", actionName), e);
        }
    }

    private static class ActionMethod {

        private final Object object;
        private final Method method;

        public ActionMethod(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public Object getObject() {
            return object;
        }

        public Method getMethod() {
            return method;
        }
    }
}
