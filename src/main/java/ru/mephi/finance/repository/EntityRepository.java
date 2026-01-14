package ru.mephi.finance.repository;

import ru.mephi.finance.model.Entity;

import java.util.Collection;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class EntityRepository<E extends Entity> implements CrudRepository<E> {
    private SortedMap<Integer, E> entities = new TreeMap<>();

    public void save(E entity) {
        entities.put(entity.getId(), entity);
    }

    public void delete(E entity) {
        entities.remove(entity.getId());
    }

    public Optional<E> findById(Integer id) {
        return Optional.ofNullable(entities.get(id));
    }

    public Collection<E> findAll() {
        return entities.values();
    }

    public void deleteAll() {
        entities.clear();
    }

    public Integer getLastId() {
        return entities.isEmpty() ? 0 : entities.lastKey();
    }
}
