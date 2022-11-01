package web.shop.service.impl;


import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.exception.not.found.UserNotFoundException;
import web.shop.repository.UserRepository;
import web.shop.service.UserService;
import web.shop.service.manager.TransactionManager;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static web.shop.util.Constant.PROFILE_PICTURES_DIR;

public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final TransactionManager transactionManager;

    public UserServiceImpl(UserRepository repository, TransactionManager transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    @Override
    public User getByLogin(String login) throws DataAccessException {
        String userNotExists = String.format("User with login '%s' doesn't exist", login);
        Optional<User> user = transactionManager.doTransaction(c -> repository.getByLogin(login, c), "Cannot get user by ID");
        if (user.isEmpty()) {
            throw new UserNotFoundException(userNotExists);
        }
        return user.get();
    }

    @Override
    public boolean isUserExists(String login) throws DataAccessException {
        return transactionManager.doTransaction(c -> repository.isUserExists(login, c),
                "Cannot check if user exists");
    }

    @Override
    public List<User> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(User user, Part profilePic) throws IOException, DataAccessException {
        createPictureInTomcatFolder(profilePic, user);
        transactionManager.doTransaction(c -> repository.create(user, c),
                "Cannot create user");
    }

    @Override
    public boolean isEmailTaken(String mail) throws DataAccessException {
        return transactionManager.doTransaction(c -> repository.isEmailTaken(mail, c),
                "Cannot check if email is taken");
    }

    /**
     * Creates uploaded image with name 'profile_pic_%login%.%extension%' in cotalina.home/images/profile_pictures
     * and set path to this image in field of user's object
     *
     * @param profilePic
     * @param user
     * @throws java.io.IOException
     */
    private void createPictureInTomcatFolder(Part profilePic, User user) throws IOException {
        String picName = String.format("/profile_pic_%s.%s", user.getLogin(), profilePic.getContentType().split("/")[1]);
        File profilePicturesDir = new File(System.getProperty("catalina.home") + PROFILE_PICTURES_DIR);
        profilePicturesDir.mkdirs();
        File picture = new File(profilePicturesDir + picName);
        profilePic.write(picture.getAbsolutePath());
        user.setProfilePicturePath(PROFILE_PICTURES_DIR + picName);
    }

}
