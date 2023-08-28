package com.ibtsoft.singularity.core.repository.transaction;

public interface TransactionListener {

    void onCommit(Transaction transaction);

    void onRollback(Transaction transaction);
}
