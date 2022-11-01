package web.shop.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static web.shop.util.Constant.ENGLISH_LANG;
import static web.shop.util.Constant.ITALIAN_LANG;
import static web.shop.util.Constant.LANGUAGE;
import static web.shop.util.Constant.UKRAINIAN_LANG;

@ExtendWith(MockitoExtension.class)
class ChangeLanguageControllerTest {

    private final ChangeLanguageController changeLanguageController = new ChangeLanguageController();

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    private final MockedStatic<Util> util = mockStatic(Util.class);

    @AfterEach
    void tearDown() {
        util.close();
    }

    @Test
    void shouldSetLangParam() throws IOException {
        String previousURL = "/";
        util.when(() -> Util.getPreviousURL(req)).thenReturn(previousURL);
        when(req.getParameter(LANGUAGE)).thenReturn(UKRAINIAN_LANG);

        changeLanguageController.doPost(req, resp);

        String shouldRedirectTo = "/?lang=uk";
        verify(resp, times(1)).sendRedirect(shouldRedirectTo);
    }

    @Test
    void shouldChangeLangParam() throws IOException {
        String previousURL = "/?lang=en";
        util.when(() -> Util.getPreviousURL(req)).thenReturn(previousURL);
        when(req.getParameter(LANGUAGE)).thenReturn(ITALIAN_LANG);

        changeLanguageController.doPost(req, resp);

        String shouldRedirectTo = "/?lang=it";
        verify(resp, times(1)).sendRedirect(shouldRedirectTo);
    }

    @Test
    void shouldChangeLangParamOnFilteredPage() throws IOException {
        String previousURL = "/product-filter?rangePrimary=199;42999&prod=2&ctg=1&ctg=4";
        util.when(() -> Util.getPreviousURL(req)).thenReturn(previousURL);
        when(req.getParameter(LANGUAGE)).thenReturn(UKRAINIAN_LANG);

        changeLanguageController.doPost(req, resp);

        String shouldRedirectTo = String.format("%s&lang=%s", previousURL, UKRAINIAN_LANG);
        verify(resp, times(1)).sendRedirect(shouldRedirectTo);
    }

    @Test
    void shouldChangeLangParamOnSignInPage() throws IOException {
        String previousURL = "/sign_in";
        util.when(() -> Util.getPreviousURL(req)).thenReturn(previousURL);
        when(req.getParameter(LANGUAGE)).thenReturn(ENGLISH_LANG);

        changeLanguageController.doPost(req, resp);

        String shouldRedirectTo = String.format("%s?lang=%s", previousURL, ENGLISH_LANG);
        verify(resp, times(1)).sendRedirect(shouldRedirectTo);
    }
}