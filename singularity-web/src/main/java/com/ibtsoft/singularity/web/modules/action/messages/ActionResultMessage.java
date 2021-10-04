package com.ibtsoft.singularity.web.modules.action.messages;

import com.ibtsoft.singularity.web.messages.Message;
import com.ibtsoft.singularity.web.messages.MessageReply;
import com.ibtsoft.singularity.web.modules.action.ActionModule;

public class ActionResultMessage extends MessageReply {

    public ActionResultMessage(Message sourceMessage, ActionResultStatusEnum status, String message) {
        super(sourceMessage.getMeta(), ActionModule.NAME, "EXECUTE", status, message);
    }
}
