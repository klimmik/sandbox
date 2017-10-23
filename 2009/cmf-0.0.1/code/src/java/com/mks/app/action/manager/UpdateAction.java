package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.form.manager.EntityForm;
import com.mks.app.util.manager.FormHelper;
import com.mks.domain.Identifiable;
import com.mks.service.util.validation.ValidationException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

public class UpdateAction extends SubmitAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {



        if ( ! getErrors(request).isEmpty() ) {
            return mapping.getInputForward();
        }


        SessionObject so = getSessionObject(request);

        if (so.isStackEmpty()) {

            if ( ! isCancelled(request)) {

                try {

                    getEntityService().saveOrUpdate((Class<Identifiable>) so.getInfo().getClazz(),
                            FormHelper.buildEntity(so.getInfo(), so.getEntityForm()));

                } catch (ValidationException e) {

                    handleValidationException(request, e);

                    return mapping.getInputForward();
                }

            }

            clearSessionObject(request);

            return new ActionForward("/search/" + so.getInfo().getUrl() + "/", true);

        } else {

            EntityForm submittedEntity = so.getEntityForm();

            so.pop();

            if ( ! isCancelled(request) ) {

                if (so.getAssociation().getType().isCollection()) { // assume one-to-many
                    Collection<EntityForm> collection =
                            (Collection<EntityForm>)
                                    so.getEntityForm().getProperties().get(so.getAssociation().getName());

                    for (Iterator it = collection.iterator(); it.hasNext();) {
                        Object item = it.next();
                        if (Integer.toHexString(item.hashCode()).equals(so.getItemHash())) {
                            it.remove();
                            break;
                        }
                    }

                    collection.add(submittedEntity);
                } else { // assume one-to-one
                    so.getEntityForm().getProperties().put(so.getAssociation().getName(), submittedEntity);
                }
            }

            return new ActionForward("/show/", true);
        }
    }

    protected boolean isValidationRequired() {
        return true;
    }
}