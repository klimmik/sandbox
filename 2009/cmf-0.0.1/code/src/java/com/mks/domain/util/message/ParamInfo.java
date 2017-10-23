package com.mks.domain.util.message;

import java.io.Serializable;

public abstract class ParamInfo implements Serializable {

    private String value;

    private boolean resource;
    private boolean fieldName;

    protected ParamInfo(String value, boolean resource, boolean fieldName) {
        this.value = value;
        this.resource = resource;
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public boolean isResource() {
        return resource;
    }

    public boolean isFieldName() {
        return fieldName;
    }

    public static class ParamString extends ParamInfo {
        public ParamString(String value) {
            super(value, false, false);
        }
    }

    public static class ParamResource extends ParamInfo {
        public ParamResource(String value) {
            super(value, true, false);
        }
    }

    public static class ParamFieldName extends ParamInfo {
        public ParamFieldName(String value) {
            super(value, true, true);
        }
    }
}
