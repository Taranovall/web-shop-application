package web.shop.filter;

import web.shop.entity.Constraint;
import web.shop.entity.User;
import web.shop.util.Constant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static web.shop.util.Constant.CONSTRAINED_PAGE_ATTR;
import static web.shop.util.Constant.CURRENT_LOCALE;
import static web.shop.util.Constant.LOCALE;
import static web.shop.util.Constant.USER;

@WebFilter(filterName = "constraintFilter", urlPatterns = "/*")
public class ConstraintFilter implements Filter {

    private static final String SIGN_IN_URL = "/sign_in";
    private static final String ERROR_YOU_HAVE_NO_ACCESS = "error.you_have_no_access";
    private List<Constraint> constraints;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.constraints = (List<Constraint>) filterConfig.getServletContext().getAttribute(Constant.CONSTRAINTS);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Optional<Constraint> constraint = getConstraintForCurrentUrl(req.getRequestURI());

        if (constraint.isPresent()) {
            redirectUser(chain, req, resp, constraint.get());
        } else {
            chain.doFilter(req, resp);
        }
    }

    /**
     * Redirects to page if user is authorized (object 'user' exists in HttpSession), otherwise redirects to sign in page
     */
    private void redirectUser(FilterChain chain, HttpServletRequest req, HttpServletResponse resp, Constraint constraint) throws IOException, ServletException {
        Optional<User> user = Optional.ofNullable((User) req.getSession().getAttribute(USER));

        if (user.isPresent()) {
            boolean isAccessible = constraint.getRoles().contains(user.get().getRole());
            redirectUserIfAccessible(chain, req, resp, isAccessible);
        } else {
            req.getSession().setAttribute(CONSTRAINED_PAGE_ATTR, req.getRequestURI());
            resp.sendRedirect(SIGN_IN_URL);
        }
    }

    /**
     * Redirects user to page if it's accessible,
     * otherwise redirects to page with error 403 (forbidden response status code)
     */
    private void redirectUserIfAccessible(FilterChain chain, HttpServletRequest req, HttpServletResponse resp, boolean isAccessible) throws IOException, ServletException {
        if (isAccessible) {
            chain.doFilter(req, resp);
        } else {
            Locale locale = (Locale) req.getSession().getAttribute(CURRENT_LOCALE);
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, ResourceBundle.getBundle(LOCALE, locale).getString(ERROR_YOU_HAVE_NO_ACCESS));
        }
    }

    private Optional<Constraint> getConstraintForCurrentUrl(String currentUrl) {
        return constraints.stream()
                .filter(c -> {
                    String urlPattern = c.getUrlPattern();
                    Pattern pattern = Pattern.compile(urlPattern);
                    Matcher matcher = pattern.matcher(currentUrl);
                    return matcher.find();
                })
                .findFirst();
    }
}
