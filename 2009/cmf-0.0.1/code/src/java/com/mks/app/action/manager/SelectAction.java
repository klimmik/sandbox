package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.action.State;
import com.mks.app.action.manager.common.BaseManagerAction;
import com.mks.app.form.manager.EntityForm;
import com.mks.app.form.manager.SelectForm;
import com.mks.app.util.manager.FormHelper;
import com.mks.domain.Identifiable;
import com.mks.domain.util.description.Association;
import com.mks.domain.util.unitofwork.KeyedUnitOfWork;
import com.mks.domain.util.unitofwork.UnitOfWorker;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectAction extends BaseManagerAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        String identifiers = ((SelectForm) form).getIdentifiers();

        SessionObject so = getSessionObject(request);

        EntityForm entityForm = so.getEntityForm();

        Association association = so.getAssociation();

        if ( ! association.isComposition() ) {

            if ( ! isCancelled(request) ) {
                if (association.getType().isCollection()) {
                    Set<Long> ids = getIds(identifiers);

                    Collection<EntityForm> associated =
                            (Collection<EntityForm>) entityForm.getProperties().get(association.getName());

                    Collection<Identifiable> selected =
                            (Collection<Identifiable>) getEntityService().getInitialized(association.getTargetInfo().getClazz(), ids);

                    KeyedUnitOfWork<EntityForm,Identifiable,Long> work = UnitOfWorker.createByIdUnit();
                    work.setOldKeyProvider(EntityForm.FORM_KEY_PROVIDER);
                    work.setOldSnapshot(associated);
                    work.setNewSnapshot(selected);

                    associated.addAll(FormHelper.buildForms(association.getTargetInfo(), work.getToAppend()));
                } else {
                    Long id = Long.parseLong(identifiers);

                    Identifiable selected = getEntityService().get(association.getTargetInfo().getClazz(), id);

                    entityForm.getProperties().put(association.getName(), FormHelper.buildForm(association.getTargetInfo(), selected));
                }
            }

            so.setState(State.EDIT);
            so.setAssociation(association); // set to null, otherwise focus on this association at the edit screen
            return new ActionForward("/show/", true);
        }

        return null;
    }

    protected static Set<Long> getIds(String input) {
        Set<Long> ids = new LinkedHashSet<Long>();
        Matcher matcher = Pattern.compile("(\\d+)(,)?").matcher(input);
        while (matcher.find()) {
            ids.add(Long.parseLong(matcher.group(1)));
        }
        return ids;
    }
}