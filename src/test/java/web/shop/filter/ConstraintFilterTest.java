package web.shop.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.entity.Constraint;
import web.shop.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static web.shop.util.Constant.CONSTRAINTS;
import static web.shop.util.Constant.CURRENT_LOCALE;
import static web.shop.util.Constant.UKRAINIAN_LANG;
import static web.shop.util.Constant.USER;

@ExtendWith(MockitoExtension.class)
class ConstraintFilterTest {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String ADMIN_PAGE = "/admin/hello";
    private static final String USER_ROLE = "USER";
    private static final String SIGN_IN_URL = "/sign_in";
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private FilterChain chain;
    @Mock
    private HttpSession session;
    @Mock
    private FilterConfig config;
    @Mock
    private ServletContext context;
    private final ConstraintFilter constraintFilter = new ConstraintFilter();
    private final List<Constraint> constraints = List.of(
            new Constraint("/admin/.*", List.of("ADMIN")),
            new Constraint("/user/.*", List.of("USER"))
    );

    @BeforeEach
    void setUp() throws ServletException {
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute(CONSTRAINTS)).thenReturn(constraints);
        constraintFilter.init(config);

    }

    @Test
    void shouldLetAccessToAdminPage() throws ServletException, IOException {
        User user = new User().setRole(ADMIN_ROLE);
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(USER)).thenReturn(user);
        when(req.getRequestURI()).thenReturn(ADMIN_PAGE);

        constraintFilter.doFilter(req, resp, chain);

        verify(chain, times(1)).doFilter(req, resp);
    }

    @Test
    void shouldRedirectToErrorPage() throws ServletException, IOException {
        User user = new User().setRole(USER_ROLE);
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(USER)).thenReturn(user);
        when(session.getAttribute(CURRENT_LOCALE)).thenReturn(new Locale(UKRAINIAN_LANG));
        when(req.getRequestURI()).thenReturn(ADMIN_PAGE);

        constraintFilter.doFilter(req, resp, chain);

        String errorMessage = "Ви не маєте доступу до цієї сторінки!";
        verify(resp, times(1)).sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
    }

    @Test
    void shouldRedirectToSignInPage() throws ServletException, IOException {
        when(req.getRequestURI()).thenReturn(ADMIN_PAGE);
        when(req.getSession()).thenReturn(session);

        constraintFilter.doFilter(req, resp, chain);

        verify(resp, times(1)).sendRedirect(SIGN_IN_URL);
    }

    @Test
    void shouldRedirectToPageWithNoConstrains() throws ServletException, IOException {
        when(req.getRequestURI()).thenReturn(SIGN_IN_URL);

        constraintFilter.doFilter(req, resp, chain);

        verify(chain, times(1)).doFilter(req, resp);
    }
}