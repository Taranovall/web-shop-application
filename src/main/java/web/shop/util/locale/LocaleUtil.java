package web.shop.util.locale;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static web.shop.util.Constant.AVAILABLE_LOCALES_INIT_PARAMS;
import static web.shop.util.Constant.DEFAULT_LOCALE;
import static web.shop.util.Constant.ENGLISH_LANG;
import static web.shop.util.Constant.ITALIAN_LANG;
import static web.shop.util.Constant.ITALY;
import static web.shop.util.Constant.UKRAINE;
import static web.shop.util.Constant.UKRAINIAN_LANG;
import static web.shop.util.Constant.USA;


public class LocaleUtil {

    public Set<Locale> getAvailableLocales(ServletContext context) {
        return Arrays.stream(getAvailableLanguages(context))
                .map(this::getLocaleWithCountryByLanguage)
                .collect(Collectors.toSet());
    }

    public String[] getAvailableLanguages(ServletContext context) {
        return context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)
                .split(", ");
    }

    public String getFlagByLocale(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    public Locale getLocaleWithCountryByLanguage(String language) {
        String country = Map.of(
                UKRAINIAN_LANG, UKRAINE,
                ENGLISH_LANG, USA,
                ITALIAN_LANG, ITALY
        ).get(language);
        return new Locale(language, country);
    }

    public Locale getDefaultLocale(ServletContext context) {
        String language = context.getInitParameter(DEFAULT_LOCALE);
        return getLocaleWithCountryByLanguage(language);
    }

    public boolean isLanguageSupported(ServletContext context, String language) {
        return language.length() == 2 && context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS).contains(language);
    }
}
