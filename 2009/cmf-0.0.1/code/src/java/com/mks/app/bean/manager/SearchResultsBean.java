package com.mks.app.bean.manager;

import com.mks.domain.Identifiable;
import com.mks.domain.util.description.EntityInfo;

import java.io.Serializable;
import java.util.List;

public class SearchResultsBean implements Serializable {
    private EntityInfo info;
    private List<? extends Identifiable> entities;

    public SearchResultsBean(EntityInfo info,
                             List<? extends Identifiable> entities) {
        this.info = info;
        this.entities = entities;
    }

    public EntityInfo getInfo() {
        return info;
    }

    public List<? extends Identifiable> getEntities() {
        return entities;
    }
}
