package com.mks.service.impl;

import com.mks.domain.Identifiable;
import com.mks.domain.util.Initializer;
import com.mks.domain.util.EntityUpdater;
import com.mks.domain.util.Unlinker;
import com.mks.domain.util.interceptor.ProfilerInterceptor;
import com.mks.service.EntityService;
import com.mks.service.util.validation.Validator;
import com.mks.service.util.validation.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Stateless
@Interceptors(ProfilerInterceptor.class)
public class EntityServiceBean implements EntityService {
    private static final Log log = LogFactory.getLog(EntityServiceBean.class);

    @PersistenceContext
    private EntityManager em;

    public <T extends Identifiable> T get(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Identifiable> List<T> get(Class<T> clazz, Set<Long> ids) {
        String idsStr = "";
        for (Long id : ids) {
            idsStr += (idsStr.length() > 0 ? "," : "") + id;
        }
        return em.createQuery("from " + clazz.getSimpleName() + " entity where entity.id in (" + idsStr + ")").getResultList();
    }

    public <T extends Identifiable> List<T> getInitialized(Class<T> clazz, Set<Long> ids) {
        List<T> res = get(clazz, ids);
        for (T entity : res) {
            Initializer.getInstance().doWork(clazz.getName(), entity);
        }
        return res;
    }

    public <T extends Identifiable> T getInitialized(Class<T> clazz, Long id) {
        T entity = get(clazz, id);
        if (entity != null) {
            Initializer.getInstance().doWork(clazz.getName(), entity);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    public <T extends Identifiable> List<T> getAll(Class<T> clazz) {
        return em.createQuery("from " + clazz.getSimpleName()).getResultList();
    }

    public <T extends Identifiable> void remove(Class<T> clazz, Long id) throws ValidationException {

        T entity = em.find(clazz, id);

        Validator.validateRemoval(em, entity);

        Unlinker.unlink(em, entity);

        em.remove( em.find(clazz, id) );
    }

    public <T extends Identifiable> void saveOrUpdate(Class<T> clazz, T entity) throws ValidationException {

        Validator.validateUpdates(em, entity); 

        if (entity.getId() == null) {
            if (log.isDebugEnabled()) log.debug("Persisting entity: " + entity);
            em.persist(entity);
        } else {
            T target = em.find(clazz, entity.getId());
            if (log.isDebugEnabled()) log.debug("Updating entity: " + target + ", ID=" + target.getId());
            EntityUpdater updater = new EntityUpdater(em);
            updater.doWork(clazz.getName(), entity, target);
            for (Object orphan : updater.getOrphansToDelete()) {
                if (log.isDebugEnabled()) log.debug("Removing orphan: " + orphan + ", ID=" + ((Identifiable) orphan).getId());
                em.remove(orphan);
            }
        }
    }
}