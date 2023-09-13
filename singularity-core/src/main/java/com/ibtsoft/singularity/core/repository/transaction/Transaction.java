package com.ibtsoft.singularity.core.repository.transaction;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;

    private final List<WeakReference<TransactionListener>> transactionListeners = new ArrayList<>();

    public Transaction(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void addTransactionListener(final TransactionListener transactionListener) {
        transactionListeners.add(new WeakReference<>(transactionListener));
    }

    public void commit() {
        transactionListeners.forEach(transactionListenerWeakReference -> transactionListenerWeakReference.get().onCommit(this));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
