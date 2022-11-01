package web.shop.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.connection.ConnectionPool;
import web.shop.entity.Newsletter;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.repository.UserRepository;
import web.shop.repository.template.JDBCTemplate;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static web.shop.util.Constant.EMAIL;
import static web.shop.util.Constant.FIRST_NAME;
import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.LOGIN;
import static web.shop.util.Constant.PASSWORD;
import static web.shop.util.Constant.PROFILE_PIC_PATH;
import static web.shop.util.Constant.SECOND_NAME;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private final Long userID = 8L;
    private final String login = "TestUser";
    private final String firstName = "Petr";
    private final String secondName = "Petrov";
    private final byte[] password = "pwd123qwe".getBytes(StandardCharsets.UTF_8);
    private final String email = "petrpetrov@gmail.com";
    private final String profilePicturePath = "/images/profile_pics/image_TestUser.jpg";
    private final Long newsletterID = 1L;

    @Spy
    private final JDBCTemplate jdbcTemplate = new JDBCTemplate();
    private final UserRepository repository = new UserRepositoryImpl(jdbcTemplate);
    @Mock
    private PreparedStatement pstmt;
    @Mock
    private ResultSet rs;
    @Mock
    Connection c;
    private static ConnectionPool connectionPool;
    private static MockedStatic<ConnectionPool> mocked;

    @BeforeAll
    static void beforeAll() {
        connectionPool = mock(ConnectionPool.class);
        mocked = mockStatic(ConnectionPool.class);
        mocked.when(ConnectionPool::getInstance).thenReturn(connectionPool);
    }

    @AfterAll
    static void afterAll() {
        mocked.close();
    }

    @BeforeEach
    void setUp() {
        c = mock(Connection.class);
        when(connectionPool.getConnection()).thenReturn(c);
    }

    @Test
    void shouldGetUserByLogin() throws DataAccessException, SQLException {
        when(c.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong(ID)).thenReturn(userID)
                .thenReturn(newsletterID);
        when(rs.getString(LOGIN)).thenReturn(login);
        when(rs.getString(FIRST_NAME)).thenReturn(firstName);
        when(rs.getString(SECOND_NAME)).thenReturn(secondName);
        when(rs.getBytes(PASSWORD)).thenReturn(password);
        when(rs.getString(EMAIL)).thenReturn(email);
        when(rs.getString(PROFILE_PIC_PATH)).thenReturn(profilePicturePath);

        Optional<User> user = repository.getByLogin(login, c);
        assertTrue(user.isPresent());

        User expectedUser = new User()
                .setId(userID)
                .setLogin(login)
                .setFirstName(firstName)
                .setSecondName(secondName)
                .setPassword(password)
                .setMail(email)
                .setProfilePicturePath(profilePicturePath)
                .setNewsletterList(Arrays.asList(Newsletter.getById(newsletterID)));
        User actualUser = user.get();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldCreateUser() throws DataAccessException, SQLException {
        Long userGeneratedId = 24L;
        User userToBeCreated = new User()
                .setLogin(login)
                .setFirstName(firstName)
                .setSecondName(secondName)
                .setPassword(password)
                .setMail(email)
                .setProfilePicturePath(profilePicturePath)
                .setNewsletterList(Arrays.asList(Newsletter.getById(newsletterID)));

        when(c.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(rs.getLong(1)).thenReturn(userGeneratedId);

        boolean actualResult = repository.create(userToBeCreated, c);
        boolean expectedResult = true;

        assertEquals(expectedResult, actualResult);

        Long actualId = userToBeCreated.getId();

        assertEquals(userGeneratedId, actualId);
    }
}