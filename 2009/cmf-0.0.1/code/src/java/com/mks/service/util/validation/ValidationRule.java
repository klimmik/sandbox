package com.mks.service.util.validation;

import com.mks.domain.util.description.Descriptor;
import com.mks.domain.util.description.DescriptorFactory;

import javax.persistence.EntityManager;

public abstract class ValidationRule {
    private static final Descriptor descriptor = DescriptorFactory.getDescriptor();

    protected static Descriptor getDescriptor() {
        return descriptor;
    }

    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public abstract void validate(Object entity) throws ValidationException;

}
