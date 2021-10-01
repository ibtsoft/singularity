package com.ibtsoft.singularity.web.modules.action.messages;

import java.util.Map;

public class ActionMessage {

    private String name;
    private Map<String, Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
