package web.shop.controller;

import web.shop.util.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import static web.shop.util.Constant.LANGUAGE;


@WebServlet(name = "changeLanguage", value = "/change-language")
public class ChangeLanguageController extends HttpServlet {

    private static final String LANG_REGEX = "lang=(\\w+)";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String previousURL = Util.getPreviousURL(req);
        String language = req.getParameter(LANGUAGE);
        String redirectURL;

        if (previousURL.equals("/")) {
            redirectURL = String.format("/?lang=%s", language);
        } else if (Pattern.compile(LANG_REGEX).matcher(previousURL).find()) {
            redirectURL = previousURL.replaceAll(LANG_REGEX, String.format("lang=%s", language));
        } else {
            if (previousURL.contains("&")) {
                redirectURL = String.format("%s&lang=%s", previousURL, language);
            } else {
                redirectURL = String.format("%s?lang=%s", previousURL, language);
            }
        }
        resp.sendRedirect(redirectURL);
    }
}
