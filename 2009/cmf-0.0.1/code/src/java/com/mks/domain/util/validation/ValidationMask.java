package com.mks.domain.util.validation;

public enum ValidationMask {

    DEFAULT(".*", ".*", "default"),

    DATE("^\\d{2}/\\d{2}/\\d{4}$", "^\\\\d{2}/\\\\d{2}/\\\\d{4}$", "date");

    private String value;
    private String valueJS;
    private String templateMsgKey;

    ValidationMask(String value, String valueJS, String templateMsgKey) {
        this.value = value;
        this.valueJS = valueJS; //TODO create valueJS from value here or try to do it right in JSP
        this.templateMsgKey = templateMsgKey;
    }

    public String getValue() {
        return value;
    }

    public String getValueJS() {
        return valueJS;
    }

    public String getTemplateMsgKey() {
        return templateMsgKey;
    }
}
