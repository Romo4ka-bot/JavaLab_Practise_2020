package ru.itis.repositories;

import java.util.List;

/**
 * @author Leontev Roman
 * 11-905
 * 12.07.20
 */

public interface CrudRepository<T> {
    List<T> findAll();
    T findById(Long id);
    void save(T entity);
    void update(T entity);

}
