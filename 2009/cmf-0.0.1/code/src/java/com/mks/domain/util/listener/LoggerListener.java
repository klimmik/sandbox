package com.mks.domain.util.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.PostPersist;
import javax.persistence.PostLoad;

public class LoggerListener {
    private static final Log log = LogFactory.getLog(LoggerListener.class);

    @PostPersist
    void postPersist(Object entity) {
        if (log.isInfoEnabled()) log.info("Persisted entity: " + entity.getClass().getName());
    }

    @PostLoad
    void postLoad(Object entity) {
        if (log.isInfoEnabled()) log.info("Loaded entity: " + entity.getClass().getName());
    }
}
