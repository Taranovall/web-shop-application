package web.shop.repository;

import web.shop.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DaoRepository<T> {

    List<T> getAll(Connection c) throws DataAccessException;

    boolean create(T entity, Connection c) throws DataAccessException, SQLException;

    void update(T entity, Connection c) throws SQLException;

    void delete(T entity, Connection c) throws SQLException;
}
