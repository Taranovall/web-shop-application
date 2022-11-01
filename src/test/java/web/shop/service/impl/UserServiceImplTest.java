package web.shop.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.connection.ConnectionPool;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.repository.UserRepository;
import web.shop.service.manager.TransactionManager;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String login = "user";

    @Spy
    private TransactionManager transactionManager = new TransactionManager();

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
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
        when(ConnectionPool.getInstance().getConnection()).thenReturn(c);
        when(connectionPool.getConnection()).thenReturn(c);
    }

    @Test
    void shouldGetUserByLogin() throws DataAccessException {
        User user = new User().setId(1L).setLogin(login);
        when(userRepository.getByLogin(anyString(), any(Connection.class))).thenReturn(Optional.of(user));

        User actualUser = userService.getByLogin(login);
        assertEquals(user, actualUser);
    }

    @Test
    void shouldCheckIfUserExists() throws DataAccessException {
        when(userRepository.isUserExists(anyString(), any(Connection.class))).thenReturn(true);

        boolean actualResult = userService.isUserExists(login);
        assertTrue(actualResult);
    }

    @Test
    void shouldCheckIfEmailIsTaken() throws DataAccessException {
        when(userRepository.isEmailTaken(anyString(), any(Connection.class))).thenReturn(false);

        boolean actualResult = userService.isEmailTaken("akjdfakf@mail.com");
        assertFalse(actualResult);
    }
}