package com.ibtsoft.singularity.web.modules.authentication.messages;

import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageReply;
import com.ibtsoft.singularity.web.modules.authentication.AuthenticationModule;
import com.singularity.security.LoginResult;

import static com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum.FAILURE;
import static com.ibtsoft.singularity.web.modules.action.messages.ActionResultStatusEnum.SUCCESS;
import static java.lang.String.format;

public class LoginResultMessage extends MessageReply {

    public LoginResultMessage(Message message, LoginResult loginResult) {
        super(message.getMeta(),
            AuthenticationModule.AUTHENTICATION_MODULE_NAME,
            "LOGIN",
            loginResult.isSuccess() ? SUCCESS : FAILURE,
            loginResult.isSuccess()
                ? format("User with username '%s' successfully logged in", loginResult.getUsername())
                : format("Failed to login user with username '%s'", loginResult.getUsername()));
    }
}
