package web.shop.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.service.UserService;
import web.shop.service.ValidatorService;
import web.shop.util.CaptchaGeneratorUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    @Mock
    private ValidatorService validatorService;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher dispatcher;
    @InjectMocks
    private SignUpController signUpController;
    @Mock
    ServletContext context;


    @BeforeAll
    static void beforeAll() {
        MockedStatic captchaGenerator = mockStatic(CaptchaGeneratorUtil.class);
        captchaGenerator
                .when(() -> CaptchaGeneratorUtil.generate(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .then(invocationOnMock -> null);
    }

    @Test
    void shouldForwardToJspPage() throws ServletException, IOException {
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        signUpController.doGet(req, resp);
        verify(dispatcher, times(1)).forward(req, resp);
    }

    @Test
    void shouldSignUserUp() throws IOException, ServletException, DataAccessException {
        when(validatorService.registrationValidate(req)).thenReturn(new User());
        when(req.getSession()).thenReturn(mock(HttpSession.class));

        signUpController.doPost(req, resp);

        verify(resp, times(1)).sendRedirect("/");
    }

    @Test
    void shouldForwardToSignUpPageWithErrorMessage() throws ServletException, IOException, DataAccessException {
        when(validatorService.registrationValidate(req)).thenReturn(null);
        when(req.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        signUpController.doPost(req, resp);

        verify(dispatcher, times(1)).forward(req, resp);
    }
}