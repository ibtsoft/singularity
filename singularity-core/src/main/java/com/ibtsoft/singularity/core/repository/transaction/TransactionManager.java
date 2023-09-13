package com.ibtsoft.singularity.core.repository.transaction;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManager.class);

    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();
    private final ThreadLocal<Transaction> transactionThreadLocal = new ThreadLocal<>();

    public Transaction startTransaction() {
        Transaction transaction = new Transaction(UUID.randomUUID());
        transactions.put(transaction.getId(), transaction);

        transactionThreadLocal.set(transaction);

        return transaction;
    }

    public boolean isInTransaction() {
        return transactionThreadLocal.get() != null;
    }

    public Transaction getCurrentTransaction() {
        Transaction transaction = transactionThreadLocal.get();
        if (transaction == null) {
            throw new RuntimeException(format("Current thread id %d has no active transaction", Thread.currentThread().getId()));
        }
        return transaction;
    }

    public void commitTransaction() {
        commitTransaction(getCurrentTransaction());
    }

    public void commitTransaction(final Transaction transaction) {
        synchronized (transaction) {
            try {
                transaction.commit();
            } catch (Exception e) {
                LOGGER.error("Failed to commit transaction");
                throw new RuntimeException("Failed to commit transaction", e);
            }
            transactionThreadLocal.remove();
            transactions.remove(transaction.getId());
        }
    }
}
