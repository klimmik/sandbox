package com.mks.domain.util.description;

import java.io.Serializable;

public abstract class Property implements Serializable {
    private EntityInfo parentInfo;
    private String name;

    public Property(EntityInfo parentInfo, String name) {
        this.parentInfo = parentInfo;
        this.name = name;
    }

    public EntityInfo getParentInfo() {
        return parentInfo;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isField();

    public abstract boolean isAssociation();

}
