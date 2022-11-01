package web.shop.service;


import web.shop.entity.User;
import web.shop.exception.DataAccessException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

public interface UserService {

    User getByLogin(String login) throws DataAccessException;

    List<User> getAll();

    void create(User user, Part profilePic) throws IOException, DataAccessException;

    boolean isUserExists(String login) throws DataAccessException;

    boolean isEmailTaken(String mail) throws DataAccessException;
}
