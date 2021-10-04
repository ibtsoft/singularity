package com.ibtsoft.singularity.web.modules.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibtsoft.singularity.core.ActionsRepository;
import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageSender;
import com.ibtsoft.singularity.web.modules.Module;
import com.ibtsoft.singularity.web.modules.action.messages.ActionMessage;
import com.ibtsoft.singularity.web.modules.action.messages.ActionResultMessage;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationModule;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationResultListener;
import com.singularity.security.UserAwareActionExecutionContext;
import com.singularity.security.UserId;

import static com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum.FAILURE;
import static com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum.SUCCESS;

public class ActionModule extends Module implements AuthenticationResultListener {

    public static final String NAME = "ACTION";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, ClassTypeAdapter.get()).create();

    private final ActionsRepository actionsRepository;

    private String username;
    private UserId userId;

    public ActionModule(MessageSender messageSender, ActionsRepository actionsRepository) {
        super(messageSender);
        this.actionsRepository = actionsRepository;
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void processMessage(Message message) {
        ActionMessage actionMessage = gson.fromJson(gson.toJsonTree(message.getData()).getAsJsonObject(), ActionMessage.class);
        ActionResultMessage resultMessage;
        switch (message.getType()) {
            case "EXECUTE":
            default:
                try {
                    actionsRepository.executeAction(new UserAwareActionExecutionContext(userId), actionMessage.getName(), actionMessage.getParams());
                    resultMessage = new ActionResultMessage(message.getId(), SUCCESS, "");
                } catch (Exception e) {
                    e.printStackTrace();
                    resultMessage = new ActionResultMessage(message.getId(), FAILURE, "");
                }

        }
        sendMessage(resultMessage);
    }

    @Override
    public void onAuthenticationSuccess(String username, UserId userId) {
        this.username = username;
        this.userId = userId;
    }
}
