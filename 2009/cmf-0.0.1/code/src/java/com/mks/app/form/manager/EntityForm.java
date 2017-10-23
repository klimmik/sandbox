package com.mks.app.form.manager;

import org.apache.struts.action.ActionForm;

import java.util.Map;
import java.util.HashMap;

import com.mks.domain.util.unitofwork.KeyProvider;

public class EntityForm extends ActionForm {
    private Map<String, Object> properties = new HashMap<String, Object>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public static final KeyProvider<EntityForm, Long> FORM_KEY_PROVIDER = new KeyProvider<EntityForm, Long>() {
        public Long getKey(EntityForm form) {
            String id = (String) form.getProperties().get("id");
            return id != null ? Long.parseLong(id) : null;
        }
    };
}
