package com.mks.app.tag;

import org.apache.taglibs.standard.tag.common.core.WhenTagSupport;

import javax.servlet.jsp.JspTagException;

public class WhenEnumTag extends WhenTagSupport {

    private Class clazz;

    protected boolean condition() throws JspTagException {
        return Enum.class.isAssignableFrom(clazz);
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}