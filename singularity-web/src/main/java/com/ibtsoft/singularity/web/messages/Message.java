package com.ibtsoft.singularity.web.messages;

public class Message {

    private String module;
    private String type;

    private Meta meta;

    private Object payload;

    public Message() {
    }

    public Message(String id, String module, String type) {
        this.meta = new Meta(id);
        this.module = module;
        this.type = type;
    }

    public Message(String id, String module, String type, Object payload) {
        this(id, module, type);
        this.payload = payload;
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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static class Meta {

        private String referenceId;

        public Meta(String id) {
            this.referenceId = id;
        }

        public String getReferenceId() {
            return referenceId;
        }

        public void setReferenceId(String referenceId) {
            this.referenceId = referenceId;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Meta{");
            sb.append("referenceId='").append(referenceId).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("module='").append(module).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", meta=").append(meta);
        sb.append(", payload=").append(payload);
        sb.append('}');
        return sb.toString();
    }
}
