package com.mks.app.action.manager.common;

import com.mks.domain.util.description.EntityInfo;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UrlAndIdAction extends BaseManagerAction {
    protected EntityInfo info;
    protected long id;

    @Override
    protected void preExecute(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        super.preExecute(mapping, form, request, response);

        //CHECK INPUT DATA BEFORE PROCESSING!!!
        String urlAndId = mapping.getParameter();

        Matcher matcher = Pattern.compile("^(.+)/(.+)$").matcher(urlAndId);
        matcher.find();

        info = getInfoByURL(matcher.group(1));
        id = Long.parseLong(matcher.group(2));
    }
}