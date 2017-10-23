package com.mks.domain.util;

import com.mks.domain.util.description.Association;
import com.mks.domain.util.description.EntityInfo;
import com.mks.domain.util.description.Field;
import com.mks.domain.util.description.Property;
import com.mks.domain.util.unitofwork.KeyedUnitOfWork;
import com.mks.domain.util.unitofwork.UnitOfWorker;
import com.mks.domain.util.unitofwork.KeyProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public abstract class PairWork extends AbstractWork {

    private KeyProvider srcKeyProvider;
    private KeyProvider trgKeyProvider;

    public void setSourceKeyProvider(KeyProvider<? extends Object, Long> srcKeyProvider) {
        this.srcKeyProvider = srcKeyProvider;
    }

    public void setTargetKeyProvider(KeyProvider<? extends Object, Long> trgKeyProvider) {
        this.trgKeyProvider = trgKeyProvider;
    }

    public void doWork(String type, Object src, Object trg) {
        doWork(type, src, trg, true);
    }

    public void doWork(String type, Object src, Object trg, boolean considerAssociations) {
        EntityInfo info = getDescriptor().getInfo(type);

        if (info == null) {
            throw new RuntimeException("No description for type: " + type);
        }

        doWork(info, src, trg, considerAssociations);
    }

    public void doWork(EntityInfo info, Object src, Object trg) {
        doWork(info, src, trg, true);
    }

    public void doWork(EntityInfo info, Object src, Object trg, boolean considerAssociations) {
        beforeWork();
        execute(info, src, trg, considerAssociations);
    }

    @SuppressWarnings({"unchecked"})
    protected void execute(EntityInfo info, Object src, Object trg, boolean considerAssociations) {
        for (Property property : info.getProperties()) {

            if ("id".equals(property.getName())) {
                if (skipIdentifier()) {
                    continue;
                }
            }

            Object newVal = getSourceProperty(src, property.getName());
            Object oldVal = getTargetProperty(trg, property.getName());

            if (property.isField()) {

                newVal = convertSourceField((Field) property, newVal);

                updateTargetField(((Field) property), trg, property.getName(), newVal);

            } else {

                if (considerAssociations) {

                    Association association = (Association) property;

                    if (association.getType().isCollection()) {

                        //prepare source collection
                        Collection<Object> newCol = (Collection<Object>) newVal;
                        if (newCol == null) {
                            newCol = Collections.emptySet();
                        }
                        //prepare target collection
                        Collection<Object> oldCol = (Collection<Object>) oldVal;
                        if (oldCol == null) {
                            oldCol = createTargetCollection();
                            setTargetProperty(trg, property.getName(), oldCol);
                        }

                        //compare source and target collections by identifiers
                        KeyedUnitOfWork<Object, Object, Long> work = createUnitOfWork(newCol, oldCol);

                        //process new items
                        for (Object newItem : work.getToAppend()) {
                            setNewItem(trg, association, newItem);
                        }

                        //process updates
                        if (association.isComposition()) {
                            for (Object oldItem : work.getToUpdateMap().keySet()) {
                                Object newItem = work.getToUpdateMap().get(oldItem);
                                execute(association.getTargetInfo(), newItem, oldItem, association.isComposition());
                            }
                        }

                        //process removed items
                        for (Object removedItem : work.getToRemove()) {
                            removeOldItem(trg, association, removedItem);
                        }

                    } else {

                        if (newVal == null) {
                            if (oldVal == null) {
                                //nothing
                            } else {
                                removeOldItem(trg, association, oldVal);
                            }
                        } else {
                            if (oldVal == null) {
                                setNewItem(trg, association, newVal);
                            } else {
                                KeyedUnitOfWork<Object, Object, Long> work = createUnitOfWork(
                                        Collections.singleton(newVal),
                                        Collections.singleton(oldVal));

                                if (work.getToUpdate().isEmpty()) {
                                    //means src and trg have different identifiers
                                    removeOldItem(trg, association, oldVal);
                                    setNewItem(trg, association, newVal);
                                } else {
                                    //means src and trg have the same identifier
                                    if (association.isComposition()) {
                                        execute(association.getTargetInfo(), newVal, oldVal, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private KeyedUnitOfWork<Object, Object, Long> createUnitOfWork(Collection<Object> newCol,
                                                                   Collection<Object> oldCol) {
        KeyedUnitOfWork<Object,Object,Long> work = UnitOfWorker.createByIdUnit();
        if (srcKeyProvider != null) {
            work.setNewKeyProvider(srcKeyProvider);
        }
        if (trgKeyProvider != null) {
            work.setOldKeyProvider(trgKeyProvider);
        }
        work.setNewSnapshot(newCol);
        work.setOldSnapshot(oldCol);
        return work;
    }

    protected Object getSourceProperty(Object bean, String name) {
        return getProperty(bean, name);
    }

    protected void setSourceProperty(Object bean, String name, Object value) {
        setProperty(bean, name, value);
    }

    protected Object getTargetProperty(Object bean, String name) {
        return getProperty(bean, name);
    }

    protected void setTargetProperty(Object bean, String name, Object value) {
        setProperty(bean, name, value);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected void updateTargetField(Field field, Object bean, String name, Object value) {
        setTargetProperty(bean, name, value);
    }

    protected boolean isTargetTypeSameAsSource() { //TODO maybe do it automatically using, e.g. instance of?
        return true;
    }

    /**
     * This method is used when {@link PairWork#isTargetTypeSameAsSource()} returns false.<br>
     * Creates and returns new instance of the given entity type by default.
     */
    protected Object createNewTargetInstance(EntityInfo info) {
        try {
            return info.getClazz().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Creates and returns a new {@link HashSet} by default.
     */
    protected Collection<Object> createTargetCollection() {
        return new HashSet<Object>();
    }

    /**
     * Simply returns the input value by default.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected Object convertSourceField(Field field, Object value) {
        return value;
    }

    protected boolean isInverseSideToProcess() {
        return true;
    }

    /**
     * In case the source and target types are the same,
     * the given value is return, otherwise
     * the new instance is created of the target type and
     * it filled in with the details from the given object.
     */
    protected Object getEntity(Association association, Object value) {
        Object res;
        if ( isTargetTypeSameAsSource() ) {
            res = value;
        } else {
            res = createNewTargetInstance(association.getTargetInfo());
            execute(association.getTargetInfo(), value, res, association.isComposition());
        }
        return res;
    }

    /**
     * If association is collection then the value is ADDED to
     * the existent collection, otherwise the value is set to
     * the bean's property.<br>
     * Processes both sides (if the second one exists and is related).
     */
    protected void setNewItem(Object bean, Association association, Object value) {
        value = getEntity(association, value);

        //process own side
        justAdd(bean, association, value);
        //process inverse side
        if (isInverseSideToProcess()) {
            Association inverseAss = association.getInverseSide();
            if (inverseAss != null) {
                justAdd(value, inverseAss, bean);
            }
        }
    }

    /**
     * @see PairWork#setNewItem(Object, Association, Object)
     */
    @SuppressWarnings({"unchecked"})
    private void justAdd(Object bean, Association association, Object value) {
        if (association.getType().isCollection()) {
            ((Collection) getTargetProperty(bean, association.getName())).add(value);
        } else {
            setTargetProperty(bean, association.getName(), value);
        }
    }

    /**
     * If association is collection then the value is REMOVED from
     * the existent collection, otherwise <code>null</code> is set to
     * the bean's property.<br>
     * If association is composition then is handled by call to
     * {@link PairWork#handleOrphan(Object)} method.<br>
     * Processes both sides (if the second one exists and is related).
     */
    protected void removeOldItem(Object bean, Association association, Object oldItem) {
        //process own side
        justRemove(bean, association, oldItem);
        //process inverse side
        if (isInverseSideToProcess()) {
            Association inverseAss = association.getInverseSide();
            if (inverseAss != null) {
                justRemove(oldItem, inverseAss, bean);
            }
        }

        if (association.isComposition()) {
            handleOrphan(oldItem);
        }
    }

    /**
     * @see PairWork#removeOldItem(Object, Association, Object)
     */
    private void justRemove(Object bean, Association association, Object value) {
        if (association.getType().isCollection()) {
            ((Collection) getTargetProperty(bean, association.getName())).remove(value);
        } else {
            setTargetProperty(bean, association.getName(), null);
        }
    }

    /**
     * Does nothing by default.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void handleOrphan(Object orphan) {
        //nothing
    }
}