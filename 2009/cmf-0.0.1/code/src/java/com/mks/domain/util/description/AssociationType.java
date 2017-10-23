package com.mks.domain.util.description;

import java.io.Serializable;

public enum AssociationType implements Serializable {
    ONE_TO_ONE {
        public boolean isCollection() {
            return false;
        }},

    ONE_TO_MANY {
        public boolean isCollection() {
            return true;
        }},

    MANY_TO_ONE {
        public boolean isCollection() {
            return false;
        }},

    MANY_TO_MANY {
        public boolean isCollection() {
            return true;
        }};

    public abstract boolean isCollection();
}
