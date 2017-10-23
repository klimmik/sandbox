package com.mks.domain.util;

import com.mks.domain.util.description.Association;
import com.mks.domain.Identifiable;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

public class EntityUpdater extends PairWork {

    private EntityManager em;

    private Set<Object> orphansToDelete = new HashSet<Object>();

    public EntityUpdater(EntityManager em) {
        this.em = em;
    }

    public Set<Object> getOrphansToDelete() {
        return orphansToDelete;
    }

    @Override
    protected boolean skipIdentifier() {
        return true;
    }

    @Override
    protected Object getEntity(Association association, Object value) {
        Object res;
        if (association.isComposition()) {
            res = super.getEntity(association, value);
        } else {
            Serializable id = ((Identifiable) value).getId();
            res = em.find(association.getTargetInfo().getClazz(), id);
        }
        return res;
    }

    @Override
    protected void handleOrphan(Object orphan) {
        orphansToDelete.add(orphan);
    }
}