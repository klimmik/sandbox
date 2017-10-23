package com.mks.app.tag;

import org.apache.taglibs.standard.tag.common.core.WhenTagSupport;

import javax.servlet.jsp.JspTagException;
import java.util.Date;

public class WhenDateTag extends WhenTagSupport {

    private Class clazz;

    protected boolean condition() throws JspTagException {
        return Date.class.isAssignableFrom(clazz);
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
