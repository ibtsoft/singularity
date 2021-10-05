package com.ibtsoft.singularity.web.modules.authentication;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.modules.authentication.messages.LoginMessage;
import com.ibtsoft.singularity.web.modules.authentication.messages.LoginResultMessage;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.ibtsoft.singularity.web.modules.authentication.messages.LoginTokenMessage;
import com.singularity.security.LoginResult;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

public class AuthenticationModule extends Module {

    public static final String AUTHENTICATION_MODULE_NAME = "AUTHENTICATION";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();

    private final List<AuthenticationResultListener> authenticationResultListeners = new ArrayList<>();

    private final SecurityManager securityManager;

    public AuthenticationModule(MessageSender sender, SecurityManager securityManager) {
        super(sender);
        this.securityManager = securityManager;
    }

    @Override
    public String getName() {
        return AUTHENTICATION_MODULE_NAME;
    }

    @Override
    public void processMessage(Message message) {

        switch (message.getType()) {
            case "LOGIN":
                LoginMessage loginMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(), LoginMessage.class);
                LoginResult loginResult = securityManager.login(loginMessage.getUsername(), loginMessage.getPassword());
                if (loginResult.isSuccess()) {
                    String username = loginResult.getUsername();
                    UserId userId = loginResult.getUserId();

                    fireOnAuthenticationSuccess(username, userId);
                }
                sendMessage(new LoginResultMessage(message, loginResult));
                break;
            case "LOGIN_WITH_TOKEN":
                LoginTokenMessage loginTokenMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(), LoginTokenMessage.class);
                loginResult = securityManager.login(loginTokenMessage.getToken());
                if (loginResult.isSuccess()) {
                    String username = loginResult.getUsername();
                    UserId userId = loginResult.getUserId();

                    fireOnAuthenticationSuccess(username, userId);
                }
                sendMessage(new LoginResultMessage(message, loginResult));
                break;
            case "LOGOFF":
                break;
        }

    }

    public void fireOnAuthenticationSuccess(String username, UserId userId) {
        authenticationResultListeners.forEach(authenticationResultListener -> authenticationResultListener.onAuthenticationSuccess(username, userId));
    }

    public void addAuthenticationResultListener(AuthenticationResultListener authenticationResultListener) {
        authenticationResultListeners.add(authenticationResultListener);
    }

    public void removeAuthenticationResultListener(AuthenticationResultListener authenticationResultListener) {
        authenticationResultListeners.remove(authenticationResultListener);
    }

}
