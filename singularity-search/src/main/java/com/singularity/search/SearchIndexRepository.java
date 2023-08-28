package com.singularity.search;

import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class SearchIndexRepository extends Repository<IndexedItem> {

    public SearchIndexRepository(Class<IndexedItem> repositoryClass,
        EntityStructureCache entityStructureCache, TransactionManager transactionManager) {
        super(repositoryClass, entityStructureCache, transactionManager);
    }
}
