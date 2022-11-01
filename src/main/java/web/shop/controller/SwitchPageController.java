package web.shop.controller;

import web.shop.util.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "switchPage", urlPatterns = "/switch_page")
public class SwitchPageController extends HttpServlet {

    private static final String PAGE_REGEX = "page=(\\d+)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pageToBeSwitchedTo = req.getParameter("page");
        String previousURL = Util.getPreviousURL(req);

        if (previousURL.equals("/")) {
            resp.sendRedirect(String.format("/?page=%s", pageToBeSwitchedTo));
        } else if (Pattern.compile(PAGE_REGEX).matcher(previousURL).find()) {
            resp.sendRedirect(previousURL.replaceAll(PAGE_REGEX, String.format("page=%s", pageToBeSwitchedTo)));
        } else {
            resp.sendRedirect(String.format("%s&page=%s", previousURL, pageToBeSwitchedTo));
        }
    }
}
