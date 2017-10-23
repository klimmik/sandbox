package com.mks.app.form.manager;

import org.apache.struts.action.ActionForm;

public class SelectForm extends ActionForm {
    private String identifiers = "";

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }
}