package com.sprinkleup.service;

import com.sprinkleup.exception.NotFoundException;
import com.sprinkleup.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Shared CRUD code for all services (INHERITANCE).
 * Subclasses only supply their repository and entity name, and may override
 * delete() when removing a record needs extra steps (POLYMORPHISM).
 */
@Transactional
public abstract class AbstractCrudService<T extends BaseEntity> implements CrudService<T> {

    private final JpaRepository<T, Long> repository;

    protected AbstractCrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    /** Name used in error messages, e.g. "Product". */
    protected abstract String entityName();

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(entityName() + " #" + id + " was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    protected T save(T entity) {
        return repository.save(entity);
    }
}
