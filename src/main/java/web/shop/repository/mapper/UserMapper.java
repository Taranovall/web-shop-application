package web.shop.repository.mapper;

import web.shop.entity.User;
import web.shop.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static web.shop.util.Constant.EMAIL;
import static web.shop.util.Constant.FIRST_NAME;
import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.LOGIN;
import static web.shop.util.Constant.PASSWORD;
import static web.shop.util.Constant.PROFILE_PIC_PATH;
import static web.shop.util.Constant.SECOND_NAME;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs) throws DataAccessException {
        try {
            return new User().setId(rs.getLong(ID))
                    .setLogin(rs.getString(LOGIN))
                    .setFirstName(rs.getString(FIRST_NAME))
                    .setSecondName(rs.getString(SECOND_NAME))
                    .setPassword(rs.getBytes(PASSWORD))
                    .setMail(rs.getString(EMAIL))
                    .setProfilePicturePath(rs.getString(PROFILE_PIC_PATH));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
