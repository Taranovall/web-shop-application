package web.shop.captcha.impl;


import web.shop.captcha.CaptchaHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static web.shop.util.Constant.CAPTCHA;

public class CaptchaSessionHandler extends CaptchaHandler {

    @Override
    public void save(HttpServletRequest req, String captcha) {
        HttpSession session = req.getSession();
        session.setAttribute(CAPTCHA, captcha);
    }

    @Override
    public String getCaptcha(HttpServletRequest req) {
        return (String) req.getSession().getAttribute(CAPTCHA);
    }

    @Override
    public void removeCaptcha(HttpServletRequest req) {
        req.getSession().removeAttribute(CAPTCHA);
    }
}
