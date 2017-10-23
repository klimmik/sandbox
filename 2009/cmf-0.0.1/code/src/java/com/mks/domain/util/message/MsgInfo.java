package com.mks.domain.util.message;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public abstract class MsgInfo implements Serializable {

    private String message = null;
    private boolean resource;

    private List<ParamInfo> params = new ArrayList<ParamInfo>();

    protected MsgInfo(String message, boolean resource) {
        this.message = message;
        this.resource = resource;
    }

    public String getMessage() {
        return message;
    }

    public boolean isResource() {
        return resource;
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public void addParams(List<ParamInfo> params) {
        this.params.addAll(params);
    }

    public void addParam(ParamInfo param) {
        this.params.add(param);
    }

    public static class MsgString extends MsgInfo {
        public MsgString(String message) {
            super(message, false);
        }
    }

    public static class MsgResource extends MsgInfo {
        public MsgResource(String message) {
            super(message, true);
        }
    }
}
