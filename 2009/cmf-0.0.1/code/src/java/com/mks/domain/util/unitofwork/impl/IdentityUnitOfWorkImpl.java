package com.mks.domain.util.unitofwork.impl;

import com.mks.domain.util.unitofwork.IdentityUnitOfWork;
import com.mks.domain.util.unitofwork.KeyProvider;

public class IdentityUnitOfWorkImpl<T> extends HashUnitOfWork<T,T,T> implements IdentityUnitOfWork<T> {

    public IdentityUnitOfWorkImpl() {
        IdentityKeyProvider<T> keyProvider = new IdentityKeyProvider<T>();
        setOldKeyProvider(keyProvider);
        setNewKeyProvider(keyProvider);
    }

    private static class IdentityKeyProvider<T> implements KeyProvider<T, T> {
        public T getKey(T object) {
            return object;
        }
    }
}
