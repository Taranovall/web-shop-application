package web.shop.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.captcha.CaptchaHandler;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.exception.not.found.UserNotFoundException;
import web.shop.service.UserService;
import web.shop.util.FailedAuthenticationUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorServiceImplTest {

    private static final String LOGIN = "Markus";
    private static final String PASSWORD = "1231231A23123";
    @Mock
    private ServletContext context;
    @Mock
    private HttpSession session;
    @Mock
    CaptchaHandler handler;
    @Mock
    private HttpServletRequest req;
    @Mock
    private UserService userService;
    @Mock
    private FailedAuthenticationUtil failedAuthenticationUtil;
    @InjectMocks
    private ValidatorServiceImpl validatorService;

    @Test
    void shouldReturnNoError() throws DataAccessException {
        stubMethods();
        when(userService.isUserExists(anyString())).thenReturn(false);
        when(handler.isCaptchaUpToDate(req)).thenReturn(true);

        String captcha = "12345";
        when(handler.getCaptcha(req)).thenReturn(captcha);
        when(req.getParameter("captcha")).thenReturn(captcha);

        User objectOfUserAfterValidation = validatorService.registrationValidate(req);

        assertNotNull(objectOfUserAfterValidation);
    }

    @Test
    void shouldSetAttributeValueUserExistsAndReturnNull() throws DataAccessException {
        stubMethods();
        when(userService.isUserExists(anyString())).thenReturn(true);
        when(handler.isCaptchaUpToDate(req)).thenReturn(false);

        User objectOfUserAfterValidation = validatorService.registrationValidate(req);

        assertNull(objectOfUserAfterValidation);
        verify(req, times(1))
                .setAttribute("error", String.format("User with login '%s' already exists", LOGIN));

    }

    @Test
    void shouldSetAttributeValueIncorrectCaptchaAndReturnNull() throws DataAccessException {
        stubMethods();
        when(userService.isUserExists(anyString())).thenReturn(false);
        when(handler.isCaptchaUpToDate(req)).thenReturn(true);
        when(handler.getCaptcha(req)).thenReturn("19284");
        when(req.getParameter("captcha")).thenReturn("19222");

        User objectOfUserAfterValidation = validatorService.registrationValidate(req);

        assertNull(objectOfUserAfterValidation);
        verify(req, times(1)).setAttribute("error", "Incorrect captcha");
    }

    @Test
    void shouldSuccessfullyValidateAuthorization() throws DataAccessException {
        when(req.getParameter("login")).thenReturn(LOGIN);
        when(req.getParameter("password")).thenReturn(PASSWORD);
        when(failedAuthenticationUtil.isUserBanned(LOGIN)).thenReturn(false);
        User user = new User()
                .setLogin(LOGIN)
                .setPassword(PASSWORD.getBytes(StandardCharsets.UTF_8));
        when(userService.getByLogin(anyString())).thenReturn(user);

        User actualUser = validatorService.loginValidate(req);
        assertEquals(user, actualUser);
    }

    @Test
    void shouldUnsuccessfullyValidateAuthorization() throws DataAccessException {
        String username = "non-existentuser";
        when(req.getParameter("login")).thenReturn(username);
        when(req.getParameter("password")).thenReturn(PASSWORD);
        User user = new User()
                .setLogin(username)
                .setPassword(PASSWORD.getBytes(StandardCharsets.UTF_8));
        when(userService.getByLogin(anyString()))
                .thenThrow(new UserNotFoundException(String.format("User with login '%s' doesn't exist", username)));

        User actualUser = validatorService.loginValidate(req);
        assertNull(actualUser);
    }

    private void stubMethods() {
        when(req.getParameter(anyString())).thenReturn(null);
        when(req.getParameter("login")).thenReturn(LOGIN);
        when(req.getParameter("password")).thenReturn(PASSWORD);
        when(req.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("captchaHandler")).thenReturn(handler);
    }
}