package com.mks.domain.util.description;

import com.mks.domain.Identifiable;
import static com.mks.domain.util.description.Association.extractAssociations;
import static com.mks.domain.util.description.Field.extractFields;
import static com.mks.domain.util.description.Field.extractUniquePaths;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

public class EntityInfo implements Serializable {
    private boolean topLevel;

    private String type;
    private EntityInfo superInfo;

    private String url;
    private String displayProperty;

    private Set<Property> ownProperties = new LinkedHashSet<Property>();

    public boolean isTopLevel() {
        return topLevel;
    }

    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EntityInfo getSuperInfo() {
        return superInfo;
    }

    public void setSuperInfo(EntityInfo superInfo) {
        this.superInfo = superInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayProperty() {
        return displayProperty;
    }

    public void setDisplayProperty(String displayProperty) {
        this.displayProperty = displayProperty;
    }

    public Set<Property> getOwnProperties() {
        return ownProperties;
    }

    public void setOwnProperties(Set<Property> ownProperties) {
        this.ownProperties = ownProperties;
    }

    public Class<? extends Identifiable> getClazz() {
        try {
            //TODO refactor - retrieve this info during descriptor creation
            return (Class<? extends Identifiable>) Class.forName(getType()); //
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Class getFieldType(String name) throws ClassNotFoundException, NoSuchFieldException {
        Class fieldType = null;
        EntityInfo info = this;
        while (info != null) {
            try {
                fieldType = info.getClazz().getDeclaredField(name).getType();
            } catch (NoSuchFieldException e) {
                info = info.getSuperInfo();
                continue;
            }
            break;
        }
        return fieldType;
    }

    public Set<Property> getProperties() {
        Set<Property> result = new LinkedHashSet<Property>();
        if ( getSuperInfo() != null ) {
            result.addAll( getSuperInfo().getProperties() );
        }
        result.addAll( getOwnProperties() );
        return result;
    }

    public Set<Field> getOwnFields() {
        return extractFields( getOwnProperties() );
    }

    public Set<Field> getFields() {
        return extractFields( getProperties() );
    }

    public Set<Association> getOwnAssociations() {
        return extractAssociations( getOwnProperties() );
    }

    public Set<Association> getAssociations() {
        return extractAssociations( getProperties() );
    }

    public List<String> getUniquePropertyPaths() {
        return extractUniquePaths( getFields() );
    }

    public Association getAssociation(String name) {
        for (Association association : getAssociations()) {
            if (association.getName().equals(name)) {
                return association;
            }
        }
        return null;
    }
}
