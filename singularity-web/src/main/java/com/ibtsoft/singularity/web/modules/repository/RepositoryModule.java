package com.ibtsoft.singularity.web.modules.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.core.repository.IRepository;
import com.ibtsoft.singularity.core.repository.RepositoryCrudListener;
import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.ClassTypeAdapter;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationResultListener;
import com.ibtsoft.singularity.web.modules.repository.messages.RepositoryCrudMessage;
import com.ibtsoft.singularity.web.modules.repository.messages.RepositoryCrudReplyMessage;
import com.ibtsoft.singularity.web.modules.repository.messages.RepositorySubscribeMessage;
import com.singularity.security.SecurityManager;
import com.singularity.security.UserId;

public class RepositoryModule extends Module implements RepositoryCrudListener, AuthenticationResultListener {

    private static final String NAME = "REPOSITORY";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();
    private final SecurityManager securityManager;

    private String username;
    private UserId userId;

    public RepositoryModule(final MessageSender messageSender, final SecurityManager securityManager) {
        super(messageSender);
        this.securityManager = securityManager;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void processMessage(final Message message) {
        RepositoryCrudReplyMessage.RepositoryCrudMessageActionEnum actionEnum = RepositoryCrudReplyMessage.RepositoryCrudMessageActionEnum.valueOf(
            message.getType());

        switch (actionEnum) {
            case SUBSCRIBE:
                RepositorySubscribeMessage repositorySubscribeMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(),
                    RepositorySubscribeMessage.class);

                List<IRepository<?>> repositories = new ArrayList<>();

                repositorySubscribeMessage.getRepositories().forEach(repository -> {
                    repositories.add(securityManager.getRepository(repository, userId));
                });

                sendSyncObjects(repositories);

                repositories.forEach(repository -> repository.addCrudListener(this));
                break;
            case UPDATE:
                RepositoryCrudMessage repositoryCrudMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(),
                    RepositoryCrudMessage.class);
                IRepository<?> repository = securityManager.getRepository(repositoryCrudMessage.getRepository(), userId);

                repository.update(gson.fromJson(gson.toJsonTree(repositoryCrudMessage).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
            case CREATE:
                repositoryCrudMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(),
                    RepositoryCrudMessage.class);
                repository = securityManager.getRepository(repositoryCrudMessage.getRepository(), userId);

                repository.save(gson.fromJson(gson.toJsonTree(repositoryCrudMessage).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
            case DELETE:
                repositoryCrudMessage = gson.fromJson(gson.toJsonTree(message.getPayload()).getAsJsonObject(),
                    RepositoryCrudMessage.class);
                repository = securityManager.getRepository(repositoryCrudMessage.getRepository(), userId);

                repository.delete(gson.fromJson(gson.toJsonTree(repositoryCrudMessage).getAsJsonObject(), (Type) repository.getRepositoryClass()));
                break;
            default:
                throw new RuntimeException("Unknown action");
        }
    }

    private void sendSyncObjects(final List<IRepository<?>> repositories) {
        HashMap<String, Object> data = new HashMap<>();
        repositories.forEach(repository -> {
            data.put(repository.getName(), repository.findAll());
        });
        RepositoryCrudReplyMessage message = new RepositoryCrudReplyMessage(UUID.randomUUID().toString(), "SYNC", data);
        sendMessage(message);
    }

    @Override
    public void onAdd(final Entity<?> entity) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(entity.getEntityClass(), entity);
        RepositoryCrudReplyMessage message = new RepositoryCrudReplyMessage(UUID.randomUUID().toString(), "ADD", data);
        sendMessage(message);
    }

    @Override
    public void onUpdate(final Entity<?> entity, final Field field, final Object value) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(entity.getEntityClass(), entity);
        RepositoryCrudReplyMessage message = new RepositoryCrudReplyMessage(UUID.randomUUID().toString(), "UPDATE", data);
        sendMessage(message);
    }

    @Override
    public void onAuthenticationSuccess(final String username, final UserId userId) {
        this.username = username;
        this.userId = userId;
    }
}
