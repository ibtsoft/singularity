package com.ibtsoft.singularity.core.repository.temporal;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class TemporalRepository<T extends TemporalEntity> extends Repository<T> {

    public TemporalRepository(Class<T> repositoryClass, EntityStructureCache entityStructureCache, TransactionManager transactionManager) {
        super(repositoryClass, entityStructureCache, transactionManager);
    }

    public TemporalRepository(Class<T> repositoryClass, EntityStructureCache entityStructureCache, TransactionManager transactionManager,
        Persistence<T> persistence) {
        super(repositoryClass, entityStructureCache, transactionManager, persistence);
    }
}
