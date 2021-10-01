package com.ibtsoft.singularity.web.modules.authentication;

import com.singularity.security.UserId;

public interface AuthenticationResultListener {

    void onAuthenticationSuccess(String username, UserId userId);
}
