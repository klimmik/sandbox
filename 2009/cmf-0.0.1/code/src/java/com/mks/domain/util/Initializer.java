package com.mks.domain.util;

public class Initializer extends MonoWork {

    private static final Initializer instance = new Initializer();

    public static Initializer getInstance() {
        return instance;
    }
}
