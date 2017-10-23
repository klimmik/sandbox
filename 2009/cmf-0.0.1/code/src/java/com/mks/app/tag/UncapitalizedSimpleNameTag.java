package com.mks.app.tag;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class UncapitalizedSimpleNameTag extends TagSupport {

    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int doStartTag() throws JspException {
        String result = StringUtils.uncapitalize(clazz.getSimpleName());
        try {
            pageContext.getOut().print(result);
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        }
        return SKIP_BODY;
    }
}
