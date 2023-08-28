package com.ibtsoft.singularity.core.repository;

public class RepositoryConfiguration {

    private boolean enableAudit;
    private boolean enableSoftDelete;

    private RepositoryConfiguration(Builder builder) {
        setEnableAudit(builder.enableAudit);
        setEnableSoftDelete(builder.enableSoftDelete);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isEnableAudit() {
        return enableAudit;
    }

    public void setEnableAudit(boolean enableAudit) {
        this.enableAudit = enableAudit;
    }

    public boolean isEnableSoftDelete() {
        return enableSoftDelete;
    }

    public void setEnableSoftDelete(boolean enableSoftDelete) {
        this.enableSoftDelete = enableSoftDelete;
    }

    public static final class Builder {

        private boolean enableAudit;
        private boolean enableSoftDelete;

        private Builder() {}

        public Builder enableAudit(boolean enableAudit) {
            this.enableAudit = enableAudit;
            return this;
        }

        public Builder enableSoftDelete(boolean enableSoftDelete) {
            this.enableSoftDelete = enableSoftDelete;
            return this;
        }

        public RepositoryConfiguration build() {
            return new RepositoryConfiguration(this);
        }
    }
}
