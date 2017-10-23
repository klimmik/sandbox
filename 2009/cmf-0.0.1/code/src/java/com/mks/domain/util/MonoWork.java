package com.mks.domain.util;

import com.mks.domain.util.description.Association;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;
import com.mks.domain.util.description.Property;

import java.util.Collection;

public abstract class MonoWork extends AbstractWork {

    public void doWork(String type, Object object) {
        doWork(type, object, true);
    }

    public void doWork(String type, Object object, boolean considerAssociations) {
        EntityInfo info = getDescriptor().getInfo(type);

        if (info == null) {
            throw new RuntimeException("No description for type: " + type);
        }

        doWork(info, object, considerAssociations);
    }

    public void doWork(EntityInfo info, Object object) {
        doWork(info, object, true);
    }

    public void doWork(EntityInfo info, Object object, boolean considerAssociations) {
        beforeWork();
        execute(info, object, considerAssociations);
    }

    protected void execute(EntityInfo info, Object object, boolean considerAssociations) {
        for (Property property : info.getProperties()) {

            if ("id".equals(property.getName())) {
                if (skipIdentifier()) {
                    continue;
                }
            }

            Object value = getProperty(object, property.getName());

            if (property.isField()) {

                handleField(info, object, ((Field) property), value);

            } else {

                if (considerAssociations && value != null) {

                    Association association = (Association) property;

                    if (association.getType().isCollection()) {

                        for (Object item : (Collection) value) {
                            execute(association.getTargetInfo(), item, association.isComposition());
                        }

                    } else {

                        execute(association.getTargetInfo(), value, association.isComposition());

                    }
                }
            }
        }
    }

    /**
     * Does nothing by default.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void handleField(EntityInfo info, Object object, Field field, Object value) {
        //nothing
    }
}