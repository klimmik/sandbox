package com.mks.app.util.manager;

import com.mks.domain.util.message.MsgInfo;
import com.mks.domain.util.message.ParamInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import java.util.Iterator;

public class ActionMessageFactory {

    private static final String RESOURCE_MG_NAMES_PREFIX = "mg.names";

    public static ActionMessage createActionMessage(MessageResources resources, MsgInfo msgInfo) {

        String msg = msgInfo.isResource() ? resources.getMessage(msgInfo.getMessage()) : msgInfo.getMessage();

        if ( ! msgInfo.getParams().isEmpty() ) {
            msg += " ";
            for (Iterator<ParamInfo> it = msgInfo.getParams().iterator(); it.hasNext();) {
                ParamInfo paramInfo = it.next();
                String param;
                if (paramInfo.isResource()) {
                    if (paramInfo.isFieldName()) {
                        param = resources.getMessage(
                                RESOURCE_MG_NAMES_PREFIX + "." + StringUtils.uncapitalize(paramInfo.getValue()));
                    } else {
                        param = resources.getMessage(
                                paramInfo.getValue());
                    }
                } else {
                    param = paramInfo.getValue();
                }
                msg += param + (it.hasNext() ? ", " : "");
            }
            msg += ".";
        }

        return new ActionMessage(msg, false);
    }
}
