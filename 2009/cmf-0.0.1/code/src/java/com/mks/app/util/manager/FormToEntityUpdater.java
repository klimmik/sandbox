package com.mks.app.util.manager;

import com.mks.app.form.manager.EntityForm;
import com.mks.domain.util.PairWork;
import com.mks.domain.util.description.Field;
import org.apache.commons.beanutils.ConvertUtils;

public class FormToEntityUpdater extends PairWork {

    public FormToEntityUpdater() {
        setSourceKeyProvider(EntityForm.FORM_KEY_PROVIDER);
    }

    @Override
    protected Object getSourceProperty(Object bean, String name) {
        return ((EntityForm) bean).getProperties().get(name);
    }

    @Override
    protected void setSourceProperty(Object bean, String name, Object value) {
        ((EntityForm) bean).getProperties().put(name, value);
    }

    @Override
    protected boolean isTargetTypeSameAsSource() {
        return false;
    }

    @Override
    protected void beforeWork() {
        super.beforeWork();
        FormHelper.registerDefaultConverters();
    }

    @Override
    @SuppressWarnings({"UnusedDeclaration"})
    protected Object convertSourceField(Field field, Object value) {
        if (value != null) {
            Class type = field.getClazz();
            if (Enum.class.isAssignableFrom(type)) { 
                if (ConvertUtils.lookup(type) == null) {
                    ConvertUtils.register(FormHelper.ENUM_CONVERTER, type);
                }
            }

            value = ConvertUtils.convert((String) value, type);
        }
        return value;
    }
}