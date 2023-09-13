package com.ibtsoft.singularity.core.repository.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityProperty;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructure;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.Transaction;
import com.ibtsoft.singularity.core.repository.transaction.TransactionListener;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

import javassist.util.proxy.MethodHandler;

public class RepoInvocationHandler implements MethodHandler, TransactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepoInvocationHandler.class);

    private final UUID id;
    private final Object target;
    private final EntityStructureCache entityStructureCache;
    private final Repository<?> repository;
    private final TransactionManager transactionManager;

    private final Map<Transaction, Stack<TransactionBuffer>> transactionsBuffer = new ConcurrentHashMap<>();

    public RepoInvocationHandler(final UUID id, final Object target, final EntityStructureCache entityStructureCache, final Repository<?> repository,
        final TransactionManager transactionManager) {
        this.id = id;
        this.target = target;
        this.entityStructureCache = entityStructureCache;
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private String getPropertyName(final Method method) {
        return method.getName().substring(3);
    }

    @Override
    public Object invoke(final Object self, final Method method, final Method proceed, final Object[] args) throws Throwable {
        Object result;
        EntityStructure entityStructure = entityStructureCache.getEntityStructure(target.getClass());
        if (method.getName().startsWith("set")) {
            String propertyName = getPropertyName(method);
            LOGGER.info("Executing setter for property {} for class {} ns", propertyName, target.getClass().getSimpleName());

            EntityProperty property = entityStructure.getProperty(propertyName);
            if (transactionManager.isInTransaction()) {
                Transaction transaction = transactionManager.getCurrentTransaction();
                transactionsBuffer.computeIfAbsent(transaction, t -> new Stack<>()).push(new TransactionBuffer(propertyName, args));
                transaction.addTransactionListener(this);
                result = args[0];
            } else {
                result = entityStructure.getProperty(propertyName).getSetter().invoke(target, args);
                repository.onFieldChanged(id, target, property.getField(), args);
            }
        } else if (method.getName().startsWith("get")) {
            result = entityStructure.getProperty(getPropertyName(method)).getGetter().invoke(target, args);
        } else {
            result = entityStructure.getMethod(method.getName()).invoke(target, args);
        }

        return result;
    }

    @Override
    public void onCommit(final Transaction transaction) {
        Stack<TransactionBuffer> transactionBufferStack = transactionsBuffer.get(transaction);

        EntityStructure entityStructure = entityStructureCache.getEntityStructure(target.getClass());

        while (!transactionBufferStack.isEmpty()) {
            TransactionBuffer transactionBuffer = transactionBufferStack.pop();
            try {
                entityStructure.getProperty(transactionBuffer.getProperty()).getSetter().invoke(target, transactionBuffer.getValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            repository.onFieldChanged(id, target, entityStructure.getProperty(transactionBuffer.getProperty()).getField(), transactionBuffer.getValue());
        }
    }

    @Override
    public void onRollback(final Transaction transaction) {
        transactionsBuffer.remove(transaction);
    }
}
