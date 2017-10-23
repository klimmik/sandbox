package com.mks.domain.util;

import com.mks.domain.Identifiable;
import com.mks.domain.util.description.Association;
import com.mks.domain.util.description.Descriptor;
import com.mks.domain.util.description.DescriptorFactory;
import com.mks.domain.util.description.EntityInfo;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

public class Unlinker {
    private static final Descriptor descriptor = DescriptorFactory.getDescriptor();

    public static <T extends Identifiable> void unlink(EntityManager em, T entity) {
        EntityInfo info = descriptor.getInfo(entity.getClass().getName());

        unlink(em, info, entity);
    }

    public static <T extends Identifiable> void unlink(EntityManager em, EntityInfo info, T entity) {
        for (Association refInfo : descriptor.getAllReferencesToType(info.getType())) {

            Query query = em.createQuery(
                    "select entity from " + refInfo.getParentInfo().getClazz().getSimpleName() + " entity " +
                            " inner join entity." + refInfo.getName() + " ref " +
                            " where ref.id = :id");

            query.setParameter("id", entity.getId());

            for (Object referencer : query.getResultList()) {
                try {
                    if (refInfo.getType().isCollection()) {
                        Object obj = PropertyUtils.getProperty(referencer, refInfo.getName());
                        ((Collection) obj).remove(entity);
                    } else {
                        PropertyUtils.setProperty(referencer, refInfo.getName(), null);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }

        if (info.getSuperInfo() != null) {
            unlink(em, info.getSuperInfo(), entity);
        }
    }
}