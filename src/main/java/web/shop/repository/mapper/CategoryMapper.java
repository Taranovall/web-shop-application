package web.shop.repository.mapper;

import web.shop.entity.Category;
import web.shop.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.NAME;

public class CategoryMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs) throws DataAccessException {
        try {
            return new Category()
                    .setId(rs.getLong(ID))
                    .setName(rs.getString(NAME));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
