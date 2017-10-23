package com.mks.service.util.validation;

import com.mks.domain.util.message.MsgInfo;

import java.util.List;
import java.util.ArrayList;

public class ValidationException extends Exception {

    private List<MsgInfo> msgInfoList = new ArrayList<MsgInfo>();

    public ValidationException(String msg, MsgInfo msgInfo) {
        super(msg);
        this.msgInfoList.add(msgInfo);
    }

    public ValidationException(List<MsgInfo> msgInfoList) {
        this.msgInfoList.addAll(msgInfoList);
    }

    public List<MsgInfo> getMsgInfoList() {
        return msgInfoList;
    }
}
