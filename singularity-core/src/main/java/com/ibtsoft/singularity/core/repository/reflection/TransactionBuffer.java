package com.ibtsoft.singularity.core.repository.reflection;

public class TransactionBuffer {

    private final String property;

    private final Object[] value;

    public TransactionBuffer(final String property, final Object[] value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public Object[] getValue() {
        return value;
    }
}
