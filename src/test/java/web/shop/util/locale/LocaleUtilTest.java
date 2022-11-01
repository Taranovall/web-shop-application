package web.shop.util.locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static web.shop.util.Constant.*;


@ExtendWith(MockitoExtension.class)
class LocaleUtilTest {
    private final LocaleUtil localeUtil = new LocaleUtil();

    @Mock
    private ServletContext context;

    @Test
    void shouldGetAvailableLanguages() {
        String availableLanguages = "en, uk, ar";
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn(availableLanguages);

        String[] expectedReturn = new String[]{ENGLISH_LANG, UKRAINIAN_LANG, "ar"};
        String[] actualReturn = localeUtil.getAvailableLanguages(context);

        assertArrayEquals(expectedReturn, actualReturn);
    }

    @Test
    void shouldGetAvailableLocales() {
        String availableLanguages = "it, en";
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn(availableLanguages);

        Set<Locale> expectedLocales = new HashSet<>(Arrays.asList(new Locale(ITALIAN_LANG, "IT"), new Locale(ENGLISH_LANG, "US")));
        Set<Locale> actualLocales = localeUtil.getAvailableLocales(context);

        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    void shouldGetFlagOfItalyByLocale() {
        Locale italyLocale = new Locale(ITALIAN_LANG, "IT");
        String expectedFlag = "\uD83C\uDDEE\uD83C\uDDF9";
        String actualFlag = localeUtil.getFlagByLocale(italyLocale);

        assertEquals(expectedFlag, actualFlag);
    }

    @Test
    void shouldGetLocaleWithCountry() {
        Locale expectedLocale = new Locale(UKRAINIAN_LANG, "UA");
        Locale actualLocale = localeUtil.getLocaleWithCountryByLanguage(UKRAINIAN_LANG);

        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    void isLanguageSupportedTest() {
        when(context.getInitParameter(AVAILABLE_LOCALES_INIT_PARAMS)).thenReturn("it, uk");

        boolean isLangSupported = localeUtil.isLanguageSupported(context, ENGLISH_LANG);
        assertFalse(isLangSupported);

        isLangSupported = localeUtil.isLanguageSupported(context, ITALIAN_LANG);
        assertTrue(isLangSupported);

        isLangSupported = localeUtil.isLanguageSupported(context, UKRAINIAN_LANG);
        assertTrue(isLangSupported);
    }
}