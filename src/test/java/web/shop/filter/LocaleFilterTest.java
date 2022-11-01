package web.shop.filter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.util.locale.LocaleStorage;
import web.shop.util.locale.LocaleUtil;
import web.shop.wrapper.LocaleRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static web.shop.util.Constant.AVAILABLE_LOCALES_INIT_PARAMS;
import static web.shop.util.Constant.DEFAULT_LOCALE;
import static web.shop.util.Constant.ENGLISH_LANG;
import static web.shop.util.Constant.ITALIAN_LANG;
import static web.shop.util.Constant.ITALY;
import static web.shop.util.Constant.LANGUAGE;
import static web.shop.util.Constant.LOCALE_STORAGE_PLACE;
import static web.shop.util.Constant.LOCALE_STORAGE_UTIL;
import static web.shop.util.Constant.LOCALE_UTIL;
import static web.shop.util.Constant.SESSION;
import static web.shop.util.Constant.UKRAINE;
import static web.shop.util.Constant.UKRAINIAN_LANG;
import static web.shop.util.Constant.USA;

@ExtendWith(MockitoExtension.class)
class LocaleFilterTest {

    @Mock
    private ServletContext context;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private LocaleStorage localeStorage;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig filterConfig;

    private LocaleUtil localeUtil = new LocaleUtil();
    private final LocaleFilter filter = new LocaleFilter();

    @BeforeEach
    void setUp() throws ServletException {
        when(req.getServletContext()).thenReturn(context);
        when(filterConfig.getServletContext()).thenReturn(context);
        when(context.getAttribute(LOCALE_STORAGE_UTIL)).thenReturn(localeStorage);
        when(context.getInitParameter(LOCALE_STORAGE_PLACE)).thenReturn(SESSION);
        when(context.getAttribute(LOCALE_UTIL)).thenReturn(localeUtil);
        filter.init(filterConfig);
    }

    @AfterEach
    void tearDown() {
        filter.destroy();
    }

    @Test
    void shouldReturnDefaultLocale() throws ServletException, IOException {
        when(context.getInitParameter(DEFAULT_LOCALE)).thenReturn(ENGLISH_LANG);
        List<Locale> locales = Arrays.asList(new Locale("ar"), new Locale("af"));
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn("it, ru");
        when(req.getLocales()).thenReturn(Collections.enumeration(locales));

        LocaleRequestWrapper expectedRequestWrapper = new LocaleRequestWrapper(req, new Locale(ENGLISH_LANG, USA));

        filter.doFilter(req, resp, filterChain);

        verify(context, times(1)).getInitParameter(DEFAULT_LOCALE);
        verify(filterChain, times(1)).doFilter(refEq(expectedRequestWrapper), eq(resp));
    }

    @Test
    void shouldReturnTheMostAcceptableLocale() throws ServletException, IOException {
        when(context.getInitParameter(DEFAULT_LOCALE)).thenReturn(ENGLISH_LANG);
        List<Locale> locales = Arrays.asList(new Locale(ITALIAN_LANG), new Locale(ENGLISH_LANG), new Locale("ru"));
        when(req.getLocales()).thenReturn(Collections.enumeration(locales));
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn("en, it");

        LocaleRequestWrapper expectedRequestWrapper = new LocaleRequestWrapper(req, new Locale(ITALIAN_LANG, ITALY));

        filter.doFilter(req, resp, filterChain);

        verify(filterChain, times(1)).doFilter(refEq(expectedRequestWrapper), eq(resp));
    }

    @Test
    void shouldChangeLanguage() throws ServletException, IOException {
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn("it, uk");
        when(req.getParameter(LANGUAGE)).thenReturn(UKRAINIAN_LANG);
        LocaleRequestWrapper wrapper = new LocaleRequestWrapper(req, new Locale(UKRAINIAN_LANG, UKRAINE));

        filter.doFilter(req, resp, filterChain);

        verify(filterChain, times(1)).doFilter(refEq(wrapper), eq(resp));
    }
}