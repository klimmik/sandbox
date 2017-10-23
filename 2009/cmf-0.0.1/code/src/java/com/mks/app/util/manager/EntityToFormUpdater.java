package com.mks.app.util.manager;

import com.mks.app.form.manager.EntityForm;
import com.mks.domain.util.PairWork;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class EntityToFormUpdater extends PairWork {

    public EntityToFormUpdater() {
        setTargetKeyProvider(EntityForm.FORM_KEY_PROVIDER);
    }

    @Override
    protected Object getTargetProperty(Object bean, String name) {
        return ((EntityForm) bean).getProperties().get(name);
    }

    @Override
    protected void setTargetProperty(Object bean, String name, Object value) {
        ((EntityForm) bean).getProperties().put(name, value);
    }

    @Override
    protected boolean isTargetTypeSameAsSource() {
        return false;
    }

    @Override
    protected Object createNewTargetInstance(EntityInfo info) {
        return new EntityForm();
    }

    @Override
    protected Collection<Object> createTargetCollection() {
        return new ArrayList<Object>();
    }

    @Override
    @SuppressWarnings({"UnusedDeclaration"})
    protected Object convertSourceField(Field field, Object value) {
        if (value != null) {
            // TODO review
            Class type = field.getClazz();
            if (Date.class.isAssignableFrom(type)) {
                value = FormHelper.SIMPLE_DATE_FORMAT.format(value);
            } else {
                value = value.toString();
            }
        }
        return value;
    }

    @Override
    protected boolean isInverseSideToProcess() {
        return false;
    }
}
