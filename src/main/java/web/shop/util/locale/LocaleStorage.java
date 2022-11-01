package web.shop.util.locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static web.shop.util.Constant.COOKIE;
import static web.shop.util.Constant.COOKIE_STORAGE_TIME;
import static web.shop.util.Constant.LOCALE;
import static web.shop.util.Constant.SESSION;

public class LocaleStorage {

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private final LocaleUtil localeUtil;
    private final Map<String, Predicate<Locale>> mapWithPutFunctions = Map.of(
            SESSION, this::putLocaleInSession,
            COOKIE, this::putLocaleInCookie
    );

    private final Map<String, LocaleRetriever> mapWithGetFunctions = Map.of(
            SESSION, this::getLocaleFromSession,
            COOKIE, this::getLocaleFromCookie
    );

    public LocaleStorage(LocaleUtil localeUtil) {
        this.localeUtil = localeUtil;
    }

    /**
     * Puts locale in storage
     *
     * @param req
     * @param resp
     * @param storage place where locale has to be put (session/cookie)
     * @return
     */
    public boolean putLocale(HttpServletRequest req, HttpServletResponse resp, String storage) {
        this.req = req;
        this.resp = resp;
        return mapWithPutFunctions.get(storage).test(req.getLocale());
    }

    /**
     * @param req     Http servlet request
     * @param storage place where locale is stored (cookie/session)
     * @return locale from storage wrapped in optional or Optional.empty() if there's no one
     */
    public Optional<Locale> getLocale(HttpServletRequest req, String storage) {
        return mapWithGetFunctions.get(storage).getLocale(req);
    }

    private Optional<Locale> getLocaleFromCookie(HttpServletRequest req) {
        Optional<Cookie[]> optionalCookies = Optional.ofNullable(req.getCookies());
        if (optionalCookies.isEmpty()) {
            return Optional.empty();
        }
        return Arrays.stream(optionalCookies.get())
                .filter(c -> c.getName().equals(LOCALE))
                .map(c -> localeUtil.getLocaleWithCountryByLanguage(c.getValue()))
                .findFirst();
    }

    private Optional<Locale> getLocaleFromSession(HttpServletRequest req) {
        return Optional.ofNullable((Locale) req.getSession().getAttribute(LOCALE));
    }

    /**
     * puts locale in cookie with age retrieved from web.xml
     *
     * @param locale
     * @return
     */
    private boolean putLocaleInCookie(Locale locale) {
        int storageTime = Integer.parseInt(req.getServletContext().getInitParameter(COOKIE_STORAGE_TIME));
        Cookie cookie = new Cookie(LOCALE, locale.getLanguage());
        cookie.setMaxAge(storageTime);
        cookie.setPath("/");
        resp.addCookie(cookie);
        return true;
    }

    private boolean putLocaleInSession(Locale locale) {
        req.getSession().setAttribute(LOCALE, locale);
        return true;
    }
}
