package web.shop.filter;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "turnOffCache", urlPatterns = "/*")
public class TurnOffCacheFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(TurnOffCacheFilter.class);
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "private, no-store, no-cache, must-revalidate";
    private static final String EXPIRES = "Expires";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.info("Turn off cache filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader(CACHE_CONTROL, CACHE_CONTROL_VALUE);
        resp.setDateHeader(EXPIRES, 0);

        chain.doFilter(request, resp);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("Turn off cache filter destroy");
    }
}
