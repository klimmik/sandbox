package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.form.manager.EntityForm;
import com.mks.domain.util.description.Association;
import com.mks.domain.util.description.EntityInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class EditItemAction extends SubmitAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        String paramAssociation = request.getParameter(PARAM_ASSOCIATION);
        String paramHashCode = request.getParameter(PARAM_ITEM_INDEX); //TODO: usage of Object.hashCode... ok?

        SessionObject so = getSessionObject(request);
        EntityInfo info = so.getInfo();
        Association associationInfo = info.getAssociation(paramAssociation);

        Object value = so.getEntityForm().getProperties().get(paramAssociation);

        if (associationInfo.getType().isCollection() && associationInfo.isComposition()) { // assume one-to-many
            so.setAssociation(associationInfo);

            EntityForm association = null;
            for (Object item : (Collection) value) {
                if (Integer.toHexString(item.hashCode()).equals(paramHashCode)) {
                    association = (EntityForm) item;
                    break;
                }
            }

            so.push(associationInfo.getTargetInfo(), cloneEntityForm(association), null, paramHashCode);

            return new ActionForward("/show/", true);

        } else if ( ! associationInfo.getType().isCollection() && associationInfo.isComposition()) { // assume one-to-one
            so.setAssociation(associationInfo);

            EntityForm association = (EntityForm) value;

            so.push(associationInfo.getTargetInfo(), cloneEntityForm(association), null, null);

            return new ActionForward("/show/", true);
        }

        return null;
    }

    private EntityForm cloneEntityForm(EntityForm entityForm) throws Exception  {
        return (EntityForm) BeanUtils.cloneBean(entityForm);
    }

    protected boolean isValidationRequired() {
        return false;
    }
}