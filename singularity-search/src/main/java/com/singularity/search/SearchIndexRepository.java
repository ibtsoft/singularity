package com.singularity.search;

import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class SearchIndexRepository extends Repository<IndexedItem> {

    public SearchIndexRepository(final Class<IndexedItem> repositoryClass, final EntityStructureCache entityStructureCache,
        final TransactionManager transactionManager) {
        super(repositoryClass, entityStructureCache, transactionManager);
    }
}
