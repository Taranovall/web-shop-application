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
import java.io.IOException;

@WebFilter(filterName = "staticResources", urlPatterns = {
        "/images/*",
        "/captcha/*",
        "/js/*",
        "/style/*"
})
public class StaticResourceFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(StaticResourceFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.info("Static resource filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        request.getRequestDispatcher(req.getRequestURI()).forward(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("Static resource filter destroy");
    }
}
