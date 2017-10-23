package com.mks.app.util.manager;

import com.mks.app.form.manager.EntityForm;
import com.mks.domain.util.MonoWork;
import com.mks.domain.util.description.Field;
import com.mks.domain.util.description.EntityInfo;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class FormValidator extends MonoWork {

    private List<Field> absentRequiredFields = new ArrayList<Field>();
    private List<Field> wrongFormatFields = new ArrayList<Field>();

    public List<Field> getAbsentRequiredFields() {
        return absentRequiredFields;
    }

    public List<Field> getWrongFormatFields() {
        return wrongFormatFields;
    }

    @Override
    protected void beforeWork() {
        super.beforeWork();
        absentRequiredFields.clear();
        wrongFormatFields.clear();
    }

    @Override
    protected boolean skipIdentifier() {
        return true;
    }

    @Override
    protected Object getProperty(Object bean, String name) {
        return ((EntityForm) bean).getProperties().get(name);
    }

    @Override
    protected void handleField(EntityInfo info, Object object, Field field, Object value) {
        validateExistence(field, (String) value);
        validateFormat(field, (String) value);
    }

    private void validateExistence(Field field, String value) {
        if ( ! field.isNullable() ) {
            if (StringUtils.isBlank(value)) {
                absentRequiredFields.add(field);
            }
        }
    }

    private void validateFormat(Field field, String value) {
        if (StringUtils.isNotBlank(value)) {
            if ( ! value.matches(field.getValidationMask().getValue()) ) {
                wrongFormatFields.add(field);
            }
        }
    }
}
