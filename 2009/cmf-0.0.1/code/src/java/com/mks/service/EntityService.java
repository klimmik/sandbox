package com.mks.service;

import com.mks.domain.Identifiable;
import com.mks.service.util.validation.ValidationException;

import java.util.List;
import java.util.Set;

public interface EntityService {

    <T extends Identifiable> T get(Class<T> clazz, Long id);

    <T extends Identifiable> List<T> get(Class<T> clazz, Set<Long> ids);

    <T extends Identifiable> List<T> getInitialized(Class<T> clazz, Set<Long> ids);

    <T extends Identifiable> T getInitialized(Class<T> clazz, Long id);

    <T extends Identifiable> List<T> getAll(Class<T> clazz);

    <T extends Identifiable> void remove(Class<T> clazz, Long id) throws ValidationException;

    <T extends Identifiable> void saveOrUpdate(Class<T> clazz, T entity) throws ValidationException;

}