package com.mks.domain.util.unitofwork;

public interface KeyProvider<T, K> {

    K getKey(T object);
    
}
