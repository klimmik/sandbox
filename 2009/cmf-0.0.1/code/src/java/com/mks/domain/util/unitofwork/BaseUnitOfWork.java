package com.mks.domain.util.unitofwork;

import java.util.Collection;
import java.util.Set;

public interface BaseUnitOfWork<Old, New> {

    Iterable<Old> getOldSnapshot();

    /**
     * @see com.mks.domain.util.unitofwork.impl.HashUnitOfWork#lazyCalculation()
     */
    void setOldSnapshot(Iterable<Old> oldSnapshot);

    Iterable<New> getNewSnapshot();

    /**
     * @see com.mks.domain.util.unitofwork.impl.HashUnitOfWork#lazyCalculation()
     */
    void setNewSnapshot(Iterable<New> newSnapshot);

    /**
     * Clears ToRemove, ToUpdate, ToAppend, to recalculate it on the next access.
	 * call it after you modify OldSnapshot or NewSnapshot to refresh
	 * results.
     */
    void clear();

    Collection<Old> getToRemove();

    Collection<New> getToAppend();

    Set<Old> getToUpdate();

}
