package com.mks.domain.util.description;

import com.mks.domain.Identifiable;
import com.mks.domain.annotation.*;
import com.mks.domain.discount.AmountDiscount;
import com.mks.domain.discount.Discount;
import com.mks.domain.discount.PercentDiscount;
import com.mks.domain.offer.*;

import java.util.*;

public class DescriptorFactory {
    private static final List<Class<? extends Identifiable>> ENTITIES;

    static {
        ENTITIES = new ArrayList<Class<? extends Identifiable>>(Arrays.asList(
                Discount.class,
                AmountDiscount.class,
                PercentDiscount.class,
                Offer.class,
                OfferItem.class,
                TermsAndConditions.class,
                OfferNote.class,
                OfferDetails.class));
    }

    private static Descriptor descriptor;

    public static synchronized Descriptor getDescriptor() {
        if (descriptor == null) {
            descriptor = buildDescriptor();
        }
        return descriptor;
    }

    private static Descriptor buildDescriptor() {
        Map<String, EntityInfo> infoMap = new HashMap<String, EntityInfo>(ENTITIES.size());

        for (Class<? extends Identifiable> clazz : ENTITIES) {
            if (clazz.isAnnotationPresent(XEntity.class)) {
                XEntity entity = clazz.getAnnotation(XEntity.class);

                EntityInfo info = infoMap.get(clazz.getName());
                if (info == null) {
                    info = new EntityInfo();
                    infoMap.put(clazz.getName(), info);
                }

                info.setTopLevel(entity.topLevel());

                info.setType(clazz.getName());
                if (clazz.getSuperclass().isAnnotationPresent(XEntity.class)) {
                    if ( ! infoMap.containsKey(clazz.getSuperclass().getName()) ) {
                        infoMap.put(clazz.getSuperclass().getName(), new EntityInfo());
                    }
                    info.setSuperInfo(infoMap.get(clazz.getSuperclass().getName()));
                }

                info.setUrl(entity.url());
                info.setDisplayProperty(entity.displayProperty());

                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(XField.class)) {
                        XField simple = field.getAnnotation(XField.class);
                        info.getOwnProperties().add(
                                new Field(
                                        info,
                                        field.getName(),
                                        simple.validationMask(),
                                        simple.nullable(),
                                        simple.length(),
                                        simple.unique()));
                    } else {
                        Association association = null;
                        if (field.isAnnotationPresent(XOneToOne.class)) {
                            XOneToOne ass = field.getAnnotation(XOneToOne.class);
                            association = new Association(
                                    info,
                                    field.getName(),
                                    getTargetInfo(infoMap, field.getType().getName()),
                                    ass.displayProperty(),
                                    AssociationType.ONE_TO_ONE,
                                    ass.composition(),
                                    ass.mappedBy() != "" ? ass.mappedBy() : null);
                        } else if (field.isAnnotationPresent(XOneToMany.class)) {
                            XOneToMany ass = field.getAnnotation(XOneToMany.class);
                            association = new Association(
                                    info,
                                    field.getName(),
                                    getTargetInfo(infoMap, ass.targetType().getName()),
                                    ass.displayProperty(),
                                    AssociationType.ONE_TO_MANY,
                                    ass.composition(),
                                    null);
                        } else if (field.isAnnotationPresent(XManyToOne.class)) {
                            XManyToOne ass = field.getAnnotation(XManyToOne.class);
                            association = new Association(
                                    info,
                                    field.getName(),
                                    getTargetInfo(infoMap, field.getType().getName()),
                                    ass.displayProperty(),
                                    AssociationType.MANY_TO_ONE,
                                    ass.composition(),
                                    ass.mappedBy() != "" ? ass.mappedBy() : null);
                        } else if (field.isAnnotationPresent(XManyToMany.class)) {
                            XManyToMany ass = field.getAnnotation(XManyToMany.class);
                            association = new Association(
                                    info,
                                    field.getName(),
                                    getTargetInfo(infoMap, ass.targetType().getName()),
                                    ass.displayProperty(),
                                    AssociationType.MANY_TO_MANY,
                                    ass.composition(),
                                    ass.mappedBy() != "" ? ass.mappedBy() : null);
                        }
                        if (association != null) {
                            info.getOwnProperties().add(association);
                        }
                    }
                }
            }
        }
        return new Descriptor(infoMap);
    }

    private static EntityInfo getTargetInfo(Map<String, EntityInfo> infoMap, String targetType) {
        if ( ! infoMap.containsKey(targetType) ) {
            infoMap.put(targetType, new EntityInfo());
        }
        return infoMap.get(targetType);
    }
}
