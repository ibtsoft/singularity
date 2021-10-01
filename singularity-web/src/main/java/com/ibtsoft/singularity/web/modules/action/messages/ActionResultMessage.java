package com.ibtsoft.singularity.web.modules.action.messages;

import com.ibtsoft.singularity.web.messages.MessageReply;
import com.ibtsoft.singularity.web.modules.action.ActionModule;

public class ActionResultMessage extends MessageReply {

    public ActionResultMessage(String replyToMessageId, ActionResultStatusEnum status, String message) {
        super(replyToMessageId, ActionModule.NAME, "EXECUTE", status, message);
    }
}
