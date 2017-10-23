package com.mks.app.action.manager;

import com.mks.app.action.manager.common.UrlAction;
import com.mks.app.action.SessionObject;
import com.mks.app.action.State;
import com.mks.app.bean.manager.SearchResultsBean;
import com.mks.domain.Identifiable;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SearchAction extends UrlAction {

    protected ActionForward executeAction(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {

        List<? extends Identifiable> entities = getEntityService().getAll(info.getClazz());

        request.setAttribute("backingBean", new SearchResultsBean(info, entities));

        SessionObject so = getSessionObject(request);
        if ( ! State.SELECT.equals(so.getState()) ) {
            so.setState(State.SEARCH);
        }

        return mapping.findForward(NEXT);
    }
}