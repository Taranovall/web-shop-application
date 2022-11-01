package web.shop.repository;


import web.shop.entity.Newsletter;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends DaoRepository<User> {

    Optional<User> getByLogin(String login, Connection c) throws DataAccessException;

    void addNewsletterToUser(Long userId, Long newsletterId, Connection c) throws DataAccessException;

    List<Newsletter> getNewslettersOfUserById(Long userId, Connection c) throws DataAccessException;

    boolean isUserExists(String login, Connection c) throws DataAccessException;

    boolean isEmailTaken(String mail, Connection c) throws DataAccessException;
}
