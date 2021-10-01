package com.ibtsoft.singularity.web.modules.repository;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.core.Entity;
import com.ibtsoft.singularity.core.IRepository;
import com.ibtsoft.singularity.core.RepositoryCrudListener;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationResultListener;
import com.ibtsoft.singularity.web.modules.repository.messages.RepositoryCrudMessage;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

public class RepositoryModule extends Module implements RepositoryCrudListener, AuthenticationResultListener {

    private static final String NAME = "REPOSITORY";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();
    private final SecurityManager securityManager;

    private String username;
    private UserId userId;

    public RepositoryModule(MessageSender messageSender, SecurityManager securityManager) {
        super(messageSender);
        this.securityManager = securityManager;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void processMessage(Message message) {
        RepositoryCrudMessage repositoryCrudMessage = gson.fromJson(gson.toJsonTree(message.getData()).getAsJsonObject(), RepositoryCrudMessage.class);
        IRepository<?> repository = securityManager.getRepository(repositoryCrudMessage.getRepository(), userId);

        RepositoryCrudMessage.RepositoryCrudMessageActionEnum actionEnum = RepositoryCrudMessage.RepositoryCrudMessageActionEnum.valueOf(message.getType());

        switch (actionEnum) {
            case SUBSCRIBE:
                sendSyncObjects(repository);
                repository.addCrudListener(this);
                break;
            case UPDATE:
                repository.update(gson.fromJson(gson.toJsonTree(repositoryCrudMessage.getData()).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
            case CREATE:
                repository.save(gson.fromJson(gson.toJsonTree(repositoryCrudMessage.getData()).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
            case DELETE:
                repository.delete(gson.fromJson(gson.toJsonTree(repositoryCrudMessage.getData()).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
        }
    }

    private void sendSyncObjects(IRepository<?> repository) {
        RepositoryCrudMessage message = new RepositoryCrudMessage(UUID.randomUUID().toString(), "SYNC", repository.findAll(), repository.getName());
        sendMessage(message);
    }

    @Override
    public void onAdd(Entity<?> entity) {
        RepositoryCrudMessage message = new RepositoryCrudMessage(UUID.randomUUID().toString(), "ADD", ImmutableList.of(entity), entity.getEntityClass());
        sendMessage(message);
    }

    @Override
    public void onAuthenticationSuccess(String username, UserId userId) {
        this.username = username;
        this.userId = userId;
    }
}
