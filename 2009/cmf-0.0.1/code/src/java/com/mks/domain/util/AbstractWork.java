package com.mks.domain.util;

import org.apache.commons.beanutils.PropertyUtils;
import com.mks.domain.util.description.Descriptor;
import com.mks.domain.util.description.DescriptorFactory;

public abstract class AbstractWork {

    private static final Descriptor descriptor = DescriptorFactory.getDescriptor();

    protected static Descriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Does nothing by default.
     */
    protected void beforeWork() {
        //nothing
    }

    /**
     * Handles identifier by default.
     */
    protected boolean skipIdentifier() {
        return false;
    }

    /**
     * Uses <code>PropertyUtils.getProperty(bean, name)</code> by default.
     */
    protected Object getProperty(Object bean, String name) {
        try {
            return PropertyUtils.getProperty(bean, name);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Uses <code>PropertyUtils.setProperty(bean, name, value)</code> by default.
     */
    protected void setProperty(Object bean, String name, Object value) {
        try {
            PropertyUtils.setProperty(bean, name, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
