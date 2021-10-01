package com.ibtsoft.singularity.web.messages;

public class Message {

    private String id;
    private String module;
    private String type;

    private Object data;

    public Message() {
    }

    public Message(String id, String module, String type) {
        this.id = id;
        this.module = module;
        this.type = type;
    }

    public Message(String id, String module, String type, Object data) {
        this(id, module, type);
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
