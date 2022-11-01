package web.shop.filter;


import org.apache.log4j.Logger;
import web.shop.util.locale.LocaleStorage;
import web.shop.util.locale.LocaleUtil;
import web.shop.wrapper.LocaleRequestWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static web.shop.util.Constant.LANGUAGE;
import static web.shop.util.Constant.LOCALE_STORAGE_PLACE;
import static web.shop.util.Constant.LOCALE_STORAGE_UTIL;
import static web.shop.util.Constant.LOCALE_UTIL;

@WebFilter(filterName = "localeFilter", urlPatterns = "/*")
public class LocaleFilter implements Filter {

    private LocaleStorage storage;
    private LocaleUtil localeUtil;
    private String localeStorePlace;

    private static final Logger LOG = Logger.getLogger(LocaleFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.storage = (LocaleStorage) filterConfig.getServletContext().getAttribute(LOCALE_STORAGE_UTIL);
        this.localeStorePlace = filterConfig.getServletContext().getInitParameter(LOCALE_STORAGE_PLACE);
        this.localeUtil = (LocaleUtil) filterConfig.getServletContext().getAttribute(LOCALE_UTIL);
        LOG.info("Locale filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Locale locale = null;
        String lang = req.getParameter(LANGUAGE);
        if (Objects.nonNull(lang) && localeUtil.isLanguageSupported(req.getServletContext(), lang)) {
            locale = localeUtil.getLocaleWithCountryByLanguage(lang);
        } else {
            Optional<Locale> optionalLocale = storage.getLocale(req, localeStorePlace);
            locale = optionalLocale.orElse(getBrowserLocaleOrElseDefault(req));
        }
        LocaleRequestWrapper wrappedRequest = new LocaleRequestWrapper(req, locale);
        storage.putLocale(wrappedRequest, resp, localeStorePlace);

        chain.doFilter(wrappedRequest, resp);
    }

    /**
     * If the browser can accept locales supported by the application,
     * the most acceptable locale will be returned,
     * otherwise the default locale whose value is specified in web.xml
     *
     * @param req
     * @return
     */
    private Locale getBrowserLocaleOrElseDefault(HttpServletRequest req) {
        ServletContext context = req.getServletContext();

        Set<String> availableLanguages = new HashSet<>(Arrays.asList(localeUtil.getAvailableLanguages(context)));

        Optional<Locale> browserLocale = Collections.list(req.getLocales()).stream()
                .filter(l -> availableLanguages.contains(l.getLanguage()))
                .map(l -> localeUtil.getLocaleWithCountryByLanguage(l.getLanguage()))
                .findFirst();

        return browserLocale.orElse(localeUtil.getDefaultLocale(context));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        LOG.info("Locale filter destroy");
    }
}
