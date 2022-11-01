package web.shop.repository.mapper;

import web.shop.entity.Producer;
import web.shop.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.NAME;

public class ProducerMapper implements RowMapper<Producer> {
    @Override
    public Producer mapRow(ResultSet rs) throws DataAccessException {
        try {
            return new Producer()
                    .setId(rs.getLong(ID))
                    .setName(rs.getString(NAME));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
