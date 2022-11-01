package web.shop.filter;

import org.apache.log4j.Logger;
import web.shop.wrapper.GzipHttpResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(filterName = "responseBodyCompressor", urlPatterns = "/*")
public class ResponseBodyCompressorFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(ResponseBodyCompressorFilter.class);
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String GZIP = "gzip";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (isGzipEncodingAccepted(req)) {
            resp.addHeader(CONTENT_ENCODING, GZIP);
            try (GzipHttpResponseWrapper gzipResponse = new GzipHttpResponseWrapper(resp)) {
                chain.doFilter(req, gzipResponse);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    private boolean isGzipEncodingAccepted(HttpServletRequest req) {
        String acceptEncoding = req.getHeader(ACCEPT_ENCODING);
        return Objects.nonNull(acceptEncoding) && acceptEncoding.contains(GZIP);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        LOG.info("Response body compressor filter init");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("Response body compressor filter destroy");

    }
}
