package web.shop.util.locale;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@FunctionalInterface
public interface LocaleRetriever {

    Optional<Locale> getLocale(HttpServletRequest req);
}
