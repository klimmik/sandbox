package com.mks.domain.util.description;

import java.util.*;

public class Descriptor {
    private Map<String, EntityInfo> infoMap = new HashMap<String, EntityInfo>();
    private List<EntityInfo> topEntities = new ArrayList<EntityInfo>();

    protected Descriptor(Map<String, EntityInfo> infoMap) {
        this.infoMap.putAll(infoMap);
        for (EntityInfo info : this.infoMap.values()) {
            infoMap.put(info.getType(), info);
            if (info.isTopLevel()) {
                topEntities.add(info);
            }
        }
    }

    public EntityInfo getInfo(String entityType) {
        return infoMap.get(entityType);
    }

    public EntityInfo getInfoByURL(String url) {
        for (EntityInfo info : infoMap.values()) {
            if (info.getUrl().equals(url)) {
                return info;
            }
        }
        return null;
    }

    public List<EntityInfo> getTopEntities() {
        return topEntities;
    }

    public List<Association> getAllReferencesToType(String entityType) {
        List<Association> references = new ArrayList<Association>();

        for (EntityInfo info : infoMap.values()) {
            for (Association assInfo : info.getOwnAssociations()) {
                if (assInfo.getTargetInfo().getType().equals(entityType)) {
                    references.add(assInfo);
                }
            }
        }

        return references;
    }
}
