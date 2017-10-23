package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.action.State;
import com.mks.app.form.manager.EntityForm;
import com.mks.app.util.manager.FormHelper;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Association;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddItemAction extends SubmitAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        String associationParam = request.getParameter(PARAM_ASSOCIATION);

        SessionObject so = getSessionObject(request);
        EntityInfo info = so.getInfo();

        Association association = info.getAssociation(associationParam);

        if (association.getType().isCollection() && ! association.isComposition()) { // assume many-to-many
            so.setState(State.SELECT);
            so.setAssociation(association);
            return new ActionForward("/search/" + association.getTargetInfo().getUrl() + "/", true);
        } else if ( ! association.getType().isCollection() && ! association.isComposition()) { // assume many-to-one
            so.setState(State.SELECT);
            so.setAssociation(association);
            return new ActionForward("/search/" + association.getTargetInfo().getUrl() + "/", true);
        } else if (association.getType().isCollection() && association.isComposition()) { // assume one-to-many
            so.setAssociation(association);

            so.push(association.getTargetInfo(), FormHelper.createForm(info), null, null);

            return new ActionForward("/show/", true);
        } else if ( ! association.getType().isCollection() && association.isComposition()) { // assume one-to-one
            so.setAssociation(association);

            so.push(association.getTargetInfo(), FormHelper.createForm(info), null, null);

            return new ActionForward("/show/", true);
        }

        return null;
    }

    protected boolean isValidationRequired() {
        return false;
    }
}