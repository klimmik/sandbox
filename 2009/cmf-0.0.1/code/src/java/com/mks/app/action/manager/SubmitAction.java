package com.mks.app.action.manager;

import com.mks.app.action.SessionObject;
import com.mks.app.action.manager.common.BaseManagerAction;
import com.mks.app.form.manager.EntityForm;
import com.mks.app.util.manager.FormValidator;
import com.mks.app.util.manager.FormUpdater;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class SubmitAction extends BaseManagerAction {

    private static final String RESOURCE_COMMON_VALIDATION_REQUIRED = "mg.commonValidation.required";
    private static final String RESOURCE_COMMON_VALIDATION_MASK = "mg.commonValidation.mask";
    private static final String RESOURCE_MG_NAMES_PREFIX = "mg.names";


    @Override
    protected void preExecute(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        super.preExecute(mapping, form, request, response);

        if (isCancelled(request)) {
            return;
        }

        EntityForm updates = (EntityForm) form;

        SessionObject so = getSessionObject(request);
        EntityInfo info = so.getInfo();
        EntityForm entityForm = so.getEntityForm();


        //update form
        FormUpdater updater = new FormUpdater();
        updater.doWork(info, updates, entityForm, false);


        //validation
        if (isValidationRequired()) {

            FormValidator validator = new FormValidator();
            validator.doWork(info, entityForm, false);

            List<Field> fieldsToFillIn = validator.getAbsentRequiredFields();
            List<Field> fieldsToFormat = validator.getWrongFormatFields();


            addErrors(request,
                    createErrorMessages(
                            Resources.getMessageResources(request), fieldsToFillIn, fieldsToFormat));
        }
    }

    protected abstract boolean isValidationRequired();

    private static ActionMessages createErrorMessages(MessageResources resources,
                                                      List<Field> fieldsToFillIn,
                                                      List<Field> fieldsToFormat) {

        ActionMessages errors = new ActionMessages();

        if ( ! fieldsToFillIn.isEmpty() ) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    generateRequiredFieldsMessage(resources, fieldsToFillIn), false));
        }

        for (Field fieldToFormat : fieldsToFormat) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    generateMaskFieldMessage(resources, fieldToFormat), false));
        }

        return errors;
    }

    private static String generateRequiredFieldsMessage(MessageResources resources, List<Field> fieldsToFillIn) {
        String msg = resources.getMessage(RESOURCE_COMMON_VALIDATION_REQUIRED) + " ";
        for (int i = 0; i < fieldsToFillIn.size(); i++) {
            Field field = fieldsToFillIn.get(i);
            msg += (i == 0 ? "" : ", ") + getFieldDisplayName(resources, field);
        }
        msg += ".";
        return msg;
    }

    private static String generateMaskFieldMessage(MessageResources resources, Field maskField) {
        String msg = resources.getMessage(RESOURCE_COMMON_VALIDATION_MASK) + " ";
        msg += getFieldDisplayName(resources, maskField);
        msg += ".";
        return msg;
    }

    private static String getFieldDisplayName(MessageResources resources, Field fieldToFormat) {
        return resources.getMessage(
                RESOURCE_MG_NAMES_PREFIX + "." +
                StringUtils.uncapitalize(fieldToFormat.getParentInfo().getClazz().getSimpleName()) + "." +
                fieldToFormat.getName());
    }
}
