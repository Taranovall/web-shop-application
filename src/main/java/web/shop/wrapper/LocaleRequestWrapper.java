package web.shop.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class LocaleRequestWrapper extends HttpServletRequestWrapper {

    private final Locale locale;
    private final HttpServletRequest req;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link javax.servlet.http.HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public LocaleRequestWrapper(HttpServletRequest request, Locale locale) {
        super(request);
        this.req = request;
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(List.of(locale));
    }
}
