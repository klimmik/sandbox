package com.mks.domain.util.description;

import java.util.Set;
import java.util.LinkedHashSet;

public class Association extends Property {
    private EntityInfo targetInfo;
    private String displayProperty;
    private AssociationType type;
    private boolean composition = false;
    private String mappedBy;

    public Association(EntityInfo parentInfo,
                       String name,
                       EntityInfo targetInfo,
                       String displayProperty,
                       AssociationType type,
                       boolean composition,
                       String mappedBy) {
        super(parentInfo, name);
        this.targetInfo = targetInfo;
        this.displayProperty = displayProperty;
        this.type = type;
        this.composition = composition;
        this.mappedBy = mappedBy;
    }

    public EntityInfo getTargetInfo() {
        return targetInfo;
    }

    public String getDisplayProperty() {
        return displayProperty;
    }

    public AssociationType getType() {
        return type;
    }

    public boolean isComposition() {
        return composition;
    }

    public String getMappedBy() {
        return mappedBy;
    }

    public boolean isField() {
        return false;
    }

    public boolean isAssociation() {
        return true;
    }

    /**
     * Check whether the association is a collection and entities of
     * this collection can be moved between collections' owners.
     * @return <code>true</code> if this association is a composite collection.
     */
    public boolean hasMovableItems() {
        return getType().isCollection() && isComposition();
    }

    /**
     * Returns association of the inverse side, or <code>null</code> if
     * there is no inverse side exists.
     */
    public Association getInverseSide() {
        Association res = null;

        if (getMappedBy() != null) {
            res = getTargetInfo().getAssociation(getMappedBy());
        } else {
            for (Association ass : getTargetInfo().getAssociations()) {
                if (ass.getMappedBy() != null && ass.getMappedBy().equals(getName())) {
                    res = ass;
                }
            }
        }

        return res;
    }

    public static Set<Association> extractAssociations(Set<Property> properties) {
        Set<Association> result = new LinkedHashSet<Association>();
        for (Property property : properties) {
            if (property.isAssociation()) {
                result.add((Association) property);
            }
        }
        return result;
    }
}
