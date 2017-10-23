package com.mks.app.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseAction extends Action {
    private static final Log log = LogFactory.getLog(BaseAction.class);

    // Predefined action forward names.
    protected final static String   HOME = "home";
    protected final static String   NEXT = "next";
    protected final static String CANCEL = "cancel";


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (log.isDebugEnabled()) log.debug("Executing " + getClass().getSimpleName() + " action...");

        ActionForward forward;
        try {
            preExecute(mapping, form, request, response);
            forward = executeAction(mapping, form, request, response);
        } catch (Exception e) {
            log.error("Failed while executing " + getClass().getSimpleName() + " action. ", e);
            throw e;
        }
        return forward;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void preExecute(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {}

    protected abstract ActionForward executeAction(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception;

}
