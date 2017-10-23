package com.mks.domain.util.unitofwork;

import com.mks.domain.util.unitofwork.impl.HashUnitOfWork;
import com.mks.domain.util.unitofwork.impl.IdentityUnitOfWorkImpl;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.Collection;

public class UnitOfWorker {

    private UnitOfWorker() {
    }

    /**
     * Returns a simple unitofworker which uses java.util.Collections
     * to implement its functionality.
     */
    public static <T> IdentityUnitOfWork<T> createIdentityUnit() {
        return new IdentityUnitOfWorkImpl<T>();
    }

    /**
     * Removes items from old snapshot, and adds new items to it
     * !!! warning: casts source snapshot to Collection !!!
     */
    public static <T> void updateOldSnapshot(IdentityUnitOfWork<T> unitOfWork) {
        Collection<T> source = (Collection<T>) unitOfWork.getOldSnapshot();
        source.removeAll( unitOfWork.getToRemove() );
        source.addAll( unitOfWork.getToAppend() );
    }

    public static <Old, New, Key> KeyedUnitOfWork<Old, New, Key> createKeyedUnit() {
        return new HashUnitOfWork<Old, New, Key>();
    }

    public static <Old, New> KeyedUnitOfWork<Old, New, Long> createByIdUnit() {
        KeyedUnitOfWork<Old, New, Long> unitOfWork = new HashUnitOfWork<Old, New, Long>();
        unitOfWork.setOldKeyProvider(new IdLongKeyProvider<Old>());
        unitOfWork.setNewKeyProvider(new IdLongKeyProvider<New>());
        return unitOfWork;
    }

    private static class IdLongKeyProvider<T> implements KeyProvider<T, Long> {
        public Long getKey(T object) {
            try {
                return (Long) PropertyUtils.getProperty(object, "id");
            } catch (Exception e) {
                throw new RuntimeException("Unable to get identifier of entity " + object.getClass().getSimpleName(), e);
            }
        }
    }
}
