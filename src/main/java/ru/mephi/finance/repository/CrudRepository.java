package ru.mephi.finance.repository;

import ru.mephi.finance.model.Entity;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<E extends Entity> {

    void save(E entity);

    void delete(E entity);

    void deleteAll();

    Optional<E> findById(Integer id);

    Collection<E> findAll();
}
