package web.shop.repository.mapper;

import web.shop.exception.DataAccessException;

import java.sql.ResultSet;

public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws DataAccessException;
}
