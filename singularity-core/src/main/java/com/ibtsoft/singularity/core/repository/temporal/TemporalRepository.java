package com.ibtsoft.singularity.core.repository.temporal;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class TemporalRepository<T extends TemporalEntity> extends Repository<T> {

    public TemporalRepository(final Class<T> repositoryClass, final EntityStructureCache entityStructureCache, final TransactionManager transactionManager) {
        super(repositoryClass, entityStructureCache, transactionManager);
    }

    public TemporalRepository(final Class<T> repositoryClass, final EntityStructureCache entityStructureCache, final TransactionManager transactionManager,
        final Persistence<T> persistence) {
        super(repositoryClass, entityStructureCache, transactionManager, persistence);
    }
}
