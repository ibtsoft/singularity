package com.singularity.security;

import com.ibtsoft.singularity.core.persistence.Persistence;
import com.ibtsoft.singularity.core.repository.Repository;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

public class UserRepository extends Repository<User> {

  public UserRepository(
      final EntityStructureCache entityStructureCache,
      final TransactionManager transactionManager,
      final Persistence<User> persistence) {
    super(User.class, entityStructureCache, transactionManager, persistence);
  }
}
