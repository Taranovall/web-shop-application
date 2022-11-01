package web.shop.filter;


import org.apache.log4j.Logger;

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
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static web.shop.util.Constant.*;
import static web.shop.util.Constant.RESOURCE_BUNDLE_LOCALE;


@WebFilter(filterName = "registerFilter", urlPatterns = "/sign_up")
public class RegistrationFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(RegistrationFilter.class);
    private static final String ERROR_YOU_ARE_LOGGED_IN = "error.you_are_logged_in";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.info("Registration filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        if (Objects.nonNull(session.getAttribute(USER))) {
            Locale locale = (Locale) session.getAttribute(CURRENT_LOCALE);
            String errorMsg = ResourceBundle.getBundle(RESOURCE_BUNDLE_LOCALE, locale).getString(ERROR_YOU_ARE_LOGGED_IN);
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, errorMsg);
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("Registration filter destroy");
    }
}
