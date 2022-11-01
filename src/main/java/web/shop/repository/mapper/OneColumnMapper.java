package web.shop.repository.mapper;

import web.shop.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OneColumnMapper implements RowMapper<Object> {
    @Override
    public Object mapRow(ResultSet rs) throws DataAccessException {
        try {
            return rs.getObject(1);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
