package ua.training.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {
    Optional<T> create(T entity) throws SQLException;
    Optional<T> findById(long id) throws SQLException;
    List<T> findAll();
    void update(T entity);
    void delete(long id) throws SQLException;
    void close();
}
