package com.mks.domain.util.description;

import com.mks.domain.util.validation.ValidationMask;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Field extends Property {
    private ValidationMask validationMask;
    private boolean nullable;
    private int length;
    private boolean unique;

    public Field(EntityInfo parentInfo, String name,
                 ValidationMask validationMask, boolean nullable, int length, boolean unique) {
        super(parentInfo, name);
        this.validationMask = validationMask;
        this.nullable = nullable;
        this.length = length;
        this.unique = unique;
    }

    public ValidationMask getValidationMask() {
        return validationMask;
    }

    public boolean isNullable() {
        return nullable;
    }

    public int getLength() {
        return length;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isField() {
        return true;
    }

    public boolean isAssociation() {
        return false;
    }

    public static Set<Field> extractFields(Set<Property> properties) {
        Set<Field> result = new LinkedHashSet<Field>();
        for (Property property : properties) {
            if (property.isField()) {
                result.add((Field) property);
            }
        }
        return result;
    }

    public static List<String> extractUniquePaths(Set<Field> properties) {
        List<String> result = new ArrayList<String>();
        for (Field property : properties) {
            if (property.isUnique()) {
                result.add( property.getName() );
            }
        }
        return result;
    }

    public Class getClazz() {
        //TODO refactor - retrieve this info during descriptor creation
        try {
            return getParentInfo().getFieldType( getName() );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
