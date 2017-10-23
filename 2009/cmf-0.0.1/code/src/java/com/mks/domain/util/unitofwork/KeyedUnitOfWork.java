package com.mks.domain.util.unitofwork;

import java.util.Map;

public interface KeyedUnitOfWork<Old, New, Key> extends BaseUnitOfWork<Old, New> {

    Map<Key, Old> getToRemoveMap();

    Map<Old, New> getToUpdateMap();

    KeyProvider<Old, Key> getOldKeyProvider();

    void setOldKeyProvider(KeyProvider<Old, Key> oldKeyProvider);

    KeyProvider<New, Key> getNewKeyProvider();

    void setNewKeyProvider(KeyProvider<New, Key> newKeyProvider);

}
