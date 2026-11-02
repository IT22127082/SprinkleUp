package com.sprinkleup.service;

import java.util.List;

/**
 * The CRUD operations every service offers (ABSTRACTION).
 * Controllers depend on this contract, not on how the data is stored.
 */
public interface CrudService<T> {

    List<T> findAll();

    T findById(Long id);

    long count();

    void delete(Long id);
}
