package com.mks.app.action.manager;

import com.mks.app.action.manager.common.UrlAndIdAction;
import com.mks.service.util.validation.ValidationException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveAction extends UrlAndIdAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        try {

            getEntityService().remove(info.getClazz(), id);

        } catch (ValidationException e) {

            handleValidationException(request, e);

            return mapping.getInputForward();
        }

        return mapping.findForward(NEXT);
    }

}