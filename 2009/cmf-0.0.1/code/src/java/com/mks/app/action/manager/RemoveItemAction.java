package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.domain.util.description.Association;
import com.mks.domain.util.description.EntityInfo;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

public class RemoveItemAction extends SubmitAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        String paramAssociation = request.getParameter(PARAM_ASSOCIATION);
        String paramHashCode = request.getParameter(PARAM_ITEM_INDEX); //TODO: usage of Object.hashCode... ok?

        SessionObject so = getSessionObject(request);
        EntityInfo info = so.getInfo();

        Association associationInfo = info.getAssociation(paramAssociation);
        Object association = so.getEntityForm().getProperties().get(paramAssociation);

        if (associationInfo.getType().isCollection()) { // for all types of collections
            Collection items = (Collection) association;
            for (Iterator it = items.iterator(); it.hasNext();) {
                Object item = it.next();
                if (Integer.toHexString(item.hashCode()).equals(paramHashCode)) {
                    it.remove();
                    break;
                }
            }
        } else {
            if ( ! associationInfo.isComposition() ) { // assume that this is many-to-one
                so.getEntityForm().getProperties().remove(paramAssociation);
            } else { // assume this is one-to-one
                so.getEntityForm().getProperties().remove(paramAssociation);
            }
        }

        return mapping.findForward(NEXT);
    }

    protected boolean isValidationRequired() {
        return false;
    }
}