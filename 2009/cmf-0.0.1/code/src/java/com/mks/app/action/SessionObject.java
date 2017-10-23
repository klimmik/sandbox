package com.mks.app.action;

import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Association;
import com.mks.app.form.manager.EntityForm;

import java.io.Serializable;
import java.util.Stack;

public class SessionObject implements Serializable {
    private State state;

    private Stack<StackInfo> stack = new Stack<StackInfo>();
    private class StackInfo {
        private EntityInfo info;
        private EntityForm entityForm;
        private Association association;
        private String itemHash;

        private StackInfo(EntityInfo info, EntityForm entityForm, Association association, String itemHash) {
            this.info = info;
            this.entityForm = entityForm;
            this.association = association;
            this.itemHash = itemHash;
        }

        public EntityInfo getInfo() {
            return info;
        }

        public EntityForm getEntityForm() {
            return entityForm;
        }

        public Association getAssociation() {
            return association;
        }

        public String getItemHash() {
            return itemHash;
        }
    }

    private EntityInfo info;
    private EntityForm entityForm;
    private Association association;
    private String itemHash;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void push(EntityInfo info, EntityForm entityForm, Association association, String itemHash) {
        stack.push(new StackInfo(this.info, this.entityForm, this.association, this.itemHash));
        this.info = info;
        this.entityForm = entityForm;
        this.association = association;
        this.itemHash = itemHash;
    }

    public void pop() {
        StackInfo si = stack.pop();
        info = si.getInfo();
        entityForm = si.getEntityForm();
        association = si.getAssociation();
    }

    public boolean isStackEmpty() {
        return stack.empty();
    }

    public EntityInfo getInfo() {
        return info;
    }

    public void setInfo(EntityInfo info) {
        this.info = info;
    }

    public EntityForm getEntityForm() {
        return entityForm;
    }

    public void setEntityForm(EntityForm entityForm) {
        this.entityForm = entityForm;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getItemHash() {
        return itemHash;
    }

    public void setItemHash(String itemHash) {
        this.itemHash = itemHash;
    }
}
