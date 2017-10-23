package com.mks.app.action.manager.common;

import com.mks.domain.util.description.EntityInfo;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class UrlAction extends BaseManagerAction {
    protected EntityInfo info;

    @Override
    protected void preExecute(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        super.preExecute(mapping, form, request, response);

        //CHECK INPUT DATA BEFORE PROCESSING!!!
        String url = mapping.getParameter();

        info = getInfoByURL(url);
    }
}
