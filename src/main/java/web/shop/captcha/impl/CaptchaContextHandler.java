package web.shop.captcha.impl;


import web.shop.captcha.CaptchaHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static web.shop.util.Constant.CAPTCHA_ID;
import static web.shop.util.Constant.COOKIE_WITH_CAPTCHA_ID;

public class CaptchaContextHandler extends CaptchaHandler {

    @Override
    public void save(HttpServletRequest req, String captcha) {
        ServletContext context = req.getServletContext();
        String captchaID = String.valueOf(Math.abs(new Random().nextLong()));

        context.setAttribute(captchaID, captcha);

        Cookie cookie = new Cookie(CAPTCHA_ID, captchaID);
        cookie.setPath("/sign_up");
        cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(TIMEOUT_IN_MILLIS));
        req.setAttribute(COOKIE_WITH_CAPTCHA_ID, cookie);
    }

    @Override
    public String getCaptcha(HttpServletRequest req) {
        Optional<Cookie> captchaCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(CAPTCHA_ID))
                .findFirst();
        return (String) req.getServletContext().getAttribute(captchaCookie.get().getValue());
    }

    @Override
    public void removeCaptcha(HttpServletRequest req) {
        req.getSession().getServletContext().removeAttribute(req.getSession().getId());
    }
}
