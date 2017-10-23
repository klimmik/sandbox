package com.mks.app.util.manager;

import com.mks.app.form.manager.EntityForm;
import com.mks.domain.util.PairWork;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;

public class FormUpdater extends PairWork {

    @Override
    protected void execute(EntityInfo info, Object src, Object trg, boolean considerAssociations) {
        if (considerAssociations) {
            throw new RuntimeException("Operation not supported.");
        }
        super.execute(info, src, trg, considerAssociations);
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
    protected void setProperty(Object bean, String name, Object value) {
        ((EntityForm) bean).getProperties().put(name, value);
    }

    @Override
    protected Object createNewTargetInstance(EntityInfo info) {
        return new EntityForm();
    }

    @Override
    @SuppressWarnings({"UnusedDeclaration"})
    protected Object convertSourceField(Field field, Object value) {
        return fixField(field, (String) value);
    }

    /**
     * Trims the given value and then truncates it in case
     * its length exceeds the maximum value.
     * @param field can be <code>null</code>.
     */
    private static String fixField(Field field, String value) {
        if (value != null) {
            value = value.trim();
            if (value.length() > field.getLength()) {
                value = value.substring(0, field.getLength());
            }
        }
        return value;
    }
}
