package com.mks.app.util.manager;

import com.mks.app.form.manager.EntityForm;
import com.mks.domain.Identifiable;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Association;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

public class FormHelper {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final Converter ENUM_CONVERTER;
    public static final Converter DATE_CONVERTER;
    static {
        DATE_CONVERTER = new Converter() {
            public Object convert(Class type, Object value) {
                if (StringUtils.isBlank((String) value)) {
                    return null;
                }
                try {
                    return SIMPLE_DATE_FORMAT.parse((String) value);
                } catch (ParseException e) {
                    throw new ConversionException("Value doesn't match the date mask.", e);
                }
            }
        };
        ENUM_CONVERTER = new Converter() {
            public Object convert(Class type, Object value) {
                if (StringUtils.isBlank((String) value)) {
                    return null;
                }
                try {
                    //noinspection unchecked
                    return Enum.valueOf(type, (String) value);
                } catch (Exception e) {
                    throw new ConversionException("Value " + value +
                            " is not a correct enum value of type " + type.getSimpleName(), e);
                }
            }
        };
    }

    /**
     * Registers {@link FormHelper#DATE_CONVERTER}.<br>
     * {@link FormHelper#ENUM_CONVERTER} must be registered manually
     * for each enumerator type
     */
    public static void registerDefaultConverters() {
        ConvertUtils.register(DATE_CONVERTER, Date.class);  //TODO: think of DATE SUBCLASSES as well
//        ConvertUtils.register(ENUM_CONVERTER, Enum.class);
    }

    public static EntityForm createForm(EntityInfo info) {
        EntityForm res = new EntityForm();

        for (Association association : info.getAssociations()) {
            if (association.getType().isCollection()) {
                res.getProperties().put(association.getName(), new ArrayList());
            }
        }

        return res;
    }

    public static EntityForm buildForm(EntityInfo info, Object entity) {
        return buildForm(info, entity, true);
    }

    public static EntityForm buildForm(EntityInfo info, Object entity, boolean withAssociations) {
        EntityForm res = new EntityForm();

        EntityToFormUpdater updater = new EntityToFormUpdater();
        updater.doWork(info, entity, res, withAssociations);

        return res;
    }

    public static List<EntityForm> buildForms(EntityInfo info, Collection entities) {
        return buildForms(info, entities, true);
    }

    public static List<EntityForm> buildForms(EntityInfo info, Collection entities, boolean withAssociations) {
        List<EntityForm> res = new ArrayList<EntityForm>(entities.size());

        for (Object entity : entities) {
            res.add(buildForm(info, entity, withAssociations));
        }

        return res;
    }

    public static Identifiable buildEntity(EntityInfo info, EntityForm form) {
        return buildEntity(info, form, true);
    }

    public static Identifiable buildEntity(EntityInfo info, EntityForm form, boolean withAssociations) {
        Identifiable res;
        try {
            res = info.getClazz().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        FormToEntityUpdater updater = new FormToEntityUpdater();
        updater.doWork(info, form, res, withAssociations);

        return res;
    }
}
