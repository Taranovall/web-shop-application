package web.shop.repository.impl;

import web.shop.entity.Newsletter;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.repository.UserRepository;
import web.shop.repository.mapper.NewsletterMapper;
import web.shop.repository.mapper.OneColumnMapper;
import web.shop.repository.mapper.UserMapper;
import web.shop.repository.template.JDBCTemplate;
import web.shop.util.SQLQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static web.shop.util.SQLQuery.ADD_NEWSLETTER_TO_USER;
import static web.shop.util.SQLQuery.CREATE_USER;
import static web.shop.util.SQLQuery.GET_EMAIL;
import static web.shop.util.SQLQuery.GET_LOGIN;
import static web.shop.util.SQLQuery.GET_NEWSLETTERS_OF_USER_BY_ID;
import static web.shop.util.SQLQuery.GET_USER_BY_LOGIN;

public class UserRepositoryImpl implements UserRepository {

    private final JDBCTemplate jdbcTemplate;

    public UserRepositoryImpl(JDBCTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll(Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(User user, Connection c) throws DataAccessException {
        Object[] parameters = new Object[]{
                user.getLogin(),
                user.getFirstName(),
                user.getSecondName(),
                user.getPassword(),
                user.getMail(),
                user.getProfilePicturePath()
        };
        Long id = jdbcTemplate.updateObject(c, CREATE_USER, parameters);
        user.setId(id);

        if (!user.getNewsletterList().isEmpty()) {
            for (Newsletter n : user.getNewsletterList()) {
                addNewsletterToUser(user.getId(), n.getId(), c);
            }
        }
        return true;
    }

    @Override
    public void addNewsletterToUser(Long userId, Long newsletterId, Connection c) throws DataAccessException {
        jdbcTemplate.updateObject(c, ADD_NEWSLETTER_TO_USER, new Object[]{userId, newsletterId});
    }

    @Override
    public Optional<User> getByLogin(String login, Connection c) throws DataAccessException {
        Optional<User> user = jdbcTemplate.queryForObject(c, GET_USER_BY_LOGIN, new Object[]{login}, new UserMapper());
        if (user.isPresent()) {
            user.get().setNewsletterList(getNewslettersOfUserById(user.get().getId(), c));
            Optional<Object> role = jdbcTemplate.queryForObject(c, SQLQuery.GET_ROLE_BY_USER_ID, new Object[]{login}, new OneColumnMapper());
            role.ifPresent(r -> user.get().setRole((String) r));
        }
        return user;
    }

    @Override
    public List<Newsletter> getNewslettersOfUserById(Long userId, Connection c) throws DataAccessException {
        return jdbcTemplate.queryForList(c, GET_NEWSLETTERS_OF_USER_BY_ID, new Object[]{userId}, new NewsletterMapper());
    }

    @Override
    public boolean isUserExists(String login, Connection c) throws DataAccessException {
        return jdbcTemplate.queryForObject(c, GET_LOGIN, new Object[]{login}, new OneColumnMapper()).isPresent();
    }

    @Override
    public boolean isEmailTaken(String mail, Connection c) throws DataAccessException {
        return jdbcTemplate.queryForObject(c, GET_EMAIL, new Object[]{mail}, new OneColumnMapper()).isPresent();
    }

    @Override
    public void update(User entity, Connection c) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(User entity, Connection c) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
