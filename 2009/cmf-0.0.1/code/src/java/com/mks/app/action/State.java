package com.mks.app.action;

public enum State {

    SEARCH {
        public boolean isEditState() { return false; }
        public boolean isSelectState() { return false; }
    },

    EDIT {
        public boolean isEditState() { return true; }
        public boolean isSelectState() { return false; }
    },

    SELECT {
        public boolean isEditState() { return false; }
        public boolean isSelectState() { return true; }
    };

    public abstract boolean isEditState();
    public abstract boolean isSelectState();

}
