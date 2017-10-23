package com.mks.app.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ObjectHashCodeTag extends TagSupport {

    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int doStartTag() throws JspException {
        String result = "" + Integer.toHexString(object.hashCode());
        try {
            pageContext.getOut().print(result);
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        }
        return SKIP_BODY;
    }
}