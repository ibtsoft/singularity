package com.singularity.security;

import com.ibtsoft.singularity.core.Persistence;
import com.ibtsoft.singularity.core.Repository;

public class UserRepository extends Repository<User> {

    public UserRepository(Persistence<User> persistence) {
        super(User.class, persistence);
    }
}
