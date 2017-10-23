package com.mks.app.action.manager.common;

import com.mks.app.action.BaseAction;
import com.mks.app.action.SessionObject;
import com.mks.app.util.manager.ActionMessageFactory;
import com.mks.domain.util.description.DescriptorFactory;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.message.MsgInfo;
import com.mks.service.EntityService;
import com.mks.service.util.validation.ValidationException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;

public abstract class BaseManagerAction extends BaseAction {

    private static final String SESSION_OBJECT = "SESSION_OBJECT";

    protected static final String PARAM_ASSOCIATION = "association";
    protected static final String PARAM_ITEM_INDEX = "itemIndex";

    protected static EntityInfo getInfoByURL(String url) throws Exception {
        return DescriptorFactory.getDescriptor().getInfoByURL(url);
    }

    protected static SessionObject getSessionObject(HttpServletRequest request) {
        SessionObject so = (SessionObject) request.getSession().getAttribute(SESSION_OBJECT);
        if (so == null) {
            so = new SessionObject();
            request.getSession().setAttribute(SESSION_OBJECT, so);
        }
        return so;
    }

    protected static void clearSessionObject(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_OBJECT);
    }

    protected static EntityService getEntityService() throws NamingException {
        InitialContext ctx = new InitialContext();
        return (EntityService) ctx.lookup("mks/EntityServiceBean/local");
    }

    protected void handleValidationException(HttpServletRequest request, ValidationException e) {
        ActionMessages errors = new ActionMessages();

        MessageResources resources = Resources.getMessageResources(request);

        for (MsgInfo msgInfo : e.getMsgInfoList()) {
            errors.add(
                    ActionMessages.GLOBAL_MESSAGE,
                    ActionMessageFactory.createActionMessage(resources, msgInfo));
        }

        addErrors(request, errors);
    }

}
