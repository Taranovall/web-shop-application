package web.shop.repository.mapper;

import web.shop.entity.Newsletter;
import web.shop.exception.DataAccessException;
import web.shop.exception.not.found.NewsletterNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static web.shop.util.Constant.ID;

public class NewsletterMapper implements RowMapper<Newsletter> {
    @Override
    public Newsletter mapRow(ResultSet rs) throws DataAccessException {
        try {
            return Newsletter.getById(rs.getLong(ID));
        } catch (NewsletterNotFoundException | SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
