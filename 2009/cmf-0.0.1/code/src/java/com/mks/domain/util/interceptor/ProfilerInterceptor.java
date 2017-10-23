package com.mks.domain.util.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class ProfilerInterceptor {
    private static final Log log = LogFactory.getLog(ProfilerInterceptor.class);

    @AroundInvoke
    public Object methodDuration(InvocationContext invocation) throws Exception {
        long startTime = 0;
        if (log.isDebugEnabled()) startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            if (log.isDebugEnabled()) {
                long endTime = System.currentTimeMillis() - startTime;
                log.debug("Method " + invocation.getMethod().getName() + "() took: " + endTime + " (ms)");
            }
        }
    }
}
