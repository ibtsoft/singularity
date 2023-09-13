package com.ibtsoft.singularity.core.repository;

public final class RepositoryConfiguration {

    private boolean enableAudit;
    private boolean enableSoftDelete;

    private RepositoryConfiguration(final Builder builder) {
        setEnableAudit(builder.enableAudit);
        setEnableSoftDelete(builder.enableSoftDelete);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isEnableAudit() {
        return enableAudit;
    }

    public void setEnableAudit(final boolean enableAudit) {
        this.enableAudit = enableAudit;
    }

    public boolean isEnableSoftDelete() {
        return enableSoftDelete;
    }

    public void setEnableSoftDelete(final boolean enableSoftDelete) {
        this.enableSoftDelete = enableSoftDelete;
    }

    public static final class Builder {

        private boolean enableAudit;
        private boolean enableSoftDelete;

        @SuppressWarnings("checkstyle:WhitespaceAround")
        private Builder() {}

        public Builder enableAudit(final boolean enableAudit) {
            this.enableAudit = enableAudit;
            return this;
        }

        public Builder enableSoftDelete(final boolean enableSoftDelete) {
            this.enableSoftDelete = enableSoftDelete;
            return this;
        }

        public RepositoryConfiguration build() {
            return new RepositoryConfiguration(this);
        }
    }
}
