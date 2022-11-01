package web.shop.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.util.locale.LocaleStorage;
import web.shop.util.locale.LocaleUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static web.shop.util.Constant.COOKIE;
import static web.shop.util.Constant.COOKIE_STORAGE_TIME;
import static web.shop.util.Constant.ITALY;
import static web.shop.util.Constant.LOCALE;
import static web.shop.util.Constant.SESSION;

@ExtendWith(MockitoExtension.class)
class LocaleStorageTest {

    private static final String IT = "it";
    private static final String CAPTCHA_ID = "captcha_id";
    private static final String SESSION_ID = "JSessionID";
    private static final String UK = "uk";
    private static final String COOKIE_STORAGE_TIME_IN_SECONDS = "180";
    private final LocaleStorage localeStorage = new LocaleStorage(new LocaleUtil());

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    HttpSession session;
    @Mock
    private ServletContext context;

    @Test
    void shouldPutAndThenGetLocaleFromSession() {
        Locale expectedLocale = new Locale(UK);
        when(req.getLocale()).thenReturn(expectedLocale);
        when(req.getSession()).thenReturn(session);

        localeStorage.putLocale(req, resp, SESSION);
        verify(session, times(1)).setAttribute(LOCALE, expectedLocale);

        when(session.getAttribute(LOCALE)).thenReturn(expectedLocale);
        Optional<Locale> optionalLocale = localeStorage.getLocale(req, SESSION);

        assertTrue(optionalLocale.isPresent());
        assertEquals(expectedLocale, optionalLocale.get());
        verify(session, times(1)).setAttribute(LOCALE, expectedLocale);
    }

    @Test
    void shouldPutAndThenGetLocaleFromCookies() {
        Locale expectedLocale = new Locale(IT, ITALY);
        when(req.getServletContext()).thenReturn(context);
        when(context.getInitParameter(COOKIE_STORAGE_TIME)).thenReturn(COOKIE_STORAGE_TIME_IN_SECONDS);
        when(req.getLocale()).thenReturn(expectedLocale);
        Cookie cookieToBeSentInResponse = new Cookie(LOCALE, expectedLocale.getLanguage());
        cookieToBeSentInResponse.setMaxAge(Integer.parseInt(COOKIE_STORAGE_TIME_IN_SECONDS));
        cookieToBeSentInResponse.setPath("/");

        localeStorage.putLocale(req, resp, COOKIE);
        verify(resp, times(1)).addCookie(refEq(cookieToBeSentInResponse));

        when(req.getCookies()).thenReturn(getCookie());
        Optional<Locale> optionalLocale = localeStorage.getLocale(req, COOKIE);

        assertTrue(optionalLocale.isPresent());
        assertEquals(expectedLocale, optionalLocale.get());
    }

    private Cookie[] getCookie() {
        return new Cookie[]{
                new Cookie(CAPTCHA_ID, "938495103958190357"),
                new Cookie(SESSION_ID, "38491123245131"),
                new Cookie(LOCALE, IT)
        };
    }
}