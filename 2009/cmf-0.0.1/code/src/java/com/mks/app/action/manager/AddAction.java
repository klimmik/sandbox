package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.action.manager.common.UrlAction;
import com.mks.app.form.manager.EntityForm;
import com.mks.app.util.manager.FormHelper;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAction extends UrlAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        SessionObject so = getSessionObject(request);
        so.setInfo(info);
        so.setEntityForm(FormHelper.createForm(info));

        return mapping.findForward(NEXT);
    }
}