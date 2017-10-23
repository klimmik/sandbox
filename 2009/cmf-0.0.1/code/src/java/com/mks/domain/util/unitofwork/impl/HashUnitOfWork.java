package com.mks.domain.util.unitofwork.impl;

import com.mks.domain.util.unitofwork.KeyProvider;
import com.mks.domain.util.unitofwork.KeyedUnitOfWork;

import java.io.Serializable;
import java.util.*;

public class HashUnitOfWork<Old, New, Key> implements KeyedUnitOfWork<Old, New, Key>, Serializable {

    Iterable<Old> oldSnapshot;
    Iterable<New> newSnapshot;

    transient Map<Key, Old> toRemove;
    transient Collection<New> toAppend;
    transient Map<Old, New> toUpdate;

    KeyProvider<Old, Key> oldKeyProvider;
    KeyProvider<New, Key> newKeyProvider;

    /**
     * <br>======== <b>WARNING!!!</b> ========<br><br>
     *
     * If an object is duplicated within a new snapshot
     * then the first occurence is being putted to toUpdate
     * and the second one - to toAppend collection.<br>
     * <u>Therefore you must apply unique constraint on your
     * snapshots sources (e.g. use sets of source elements
     * instead of lists).</u>
     */
    protected void lazyCalculation() {
        if (toRemove == null || toAppend == null || toUpdate == null) {

            toRemove = createToRemove(/*getOldSnapshot()*/);
            toAppend = createEmptyToAppend();
            toUpdate = createEmptyToUpdate();

            for (New updated : getNewSnapshot()) {
                Key key = getNewKeyProvider().getKey(updated);
                // tries to lookup it in the old snapshot
                if (key == null || ! toRemove.containsKey(key)) {
                    // new elements
                    toAppend.add(updated);
                } else {// it is element to update
                    Old oldInst = toRemove.get(key);
                    toUpdate.put(oldInst, updated);
                    // don't remove this elem
                    toRemove.remove(key);
                }
            }
        }
    }

    public Collection<New> getToAppend() {
        lazyCalculation();
        return toAppend;
    }

    public Map<Key, Old> getToRemoveMap() {
        lazyCalculation();
        return toRemove;
    }
    public Collection<Old> getToRemove() {
        return getToRemoveMap().values();
    }

    public Map<Old, New> getToUpdateMap() {
        lazyCalculation();
        return toUpdate;
    }
    public Set<Old> getToUpdate() {
        return getToUpdateMap().keySet();
    }

    Collection<New> createEmptyToAppend() {
        return new HashSet<New>();
    }
    Map<Old, New> createEmptyToUpdate() {
        return new HashMap<Old, New>();
    }
    /**
     * if source castable to collection.
     * enlarge the new map to the snapshot's size
     */
    Map<Key, Old> createEmptyToRemove() {
        Iterable<Old> ssh = getOldSnapshot();
        return (ssh instanceof Collection) ?
                    new HashMap<Key, Old>( ((Collection<Old>) ssh).size() )
                    : new HashMap<Key, Old>();
    }
    /**
     * returns copy of SourceSnapshot "indexed" by keys
     */
    Map<Key, Old> createToRemove() {
        Map<Key, Old> map = createEmptyToRemove();
        putSnapshot(map, getOldSnapshot());
        return map;
    }
    /**
     * evaluate all pairs and put it to the map
     */
    void putSnapshot(Map<Key, Old> map, Iterable<Old> sourceSnapshot) {
        KeyProvider<Old, Key> provider = getOldKeyProvider();
        for(Old old : sourceSnapshot) {
            map.put(provider.getKey(old), old);
        }
    }

    public void clear() {
        toRemove = null;
        toAppend = null;
        toUpdate = null;
    }

    public Iterable<Old> getOldSnapshot() {
        return oldSnapshot;
    }
    public void setOldSnapshot(Iterable<Old> oldSnapshot) {
        clear();
        this.oldSnapshot = oldSnapshot;
    }

    public Iterable<New> getNewSnapshot() {
        return newSnapshot;
    }
    
    public void setNewSnapshot(Iterable<New> newSnapshot) {
        clear();
        this.newSnapshot = newSnapshot;
    }

    public KeyProvider<Old, Key> getOldKeyProvider() {
        return oldKeyProvider;
    }
    public void setOldKeyProvider(KeyProvider<Old, Key> oldKeyProvider) {
        this.oldKeyProvider = oldKeyProvider;
    }

    public KeyProvider<New, Key> getNewKeyProvider() {
        return newKeyProvider;
    }
    public void setNewKeyProvider(KeyProvider<New, Key> newKeyProvider) {
        this.newKeyProvider = newKeyProvider;
    }
}
