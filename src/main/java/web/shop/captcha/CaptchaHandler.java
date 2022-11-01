package web.shop.captcha;

import web.shop.util.Constant;

import javax.servlet.http.HttpServletRequest;


public abstract class CaptchaHandler {

    protected static final int TIMEOUT_IN_MILLIS = 15_000_000;

    public abstract void save(HttpServletRequest req, String captcha);

    public abstract String getCaptcha(HttpServletRequest req);

    public abstract void removeCaptcha(HttpServletRequest req);

    public boolean isCaptchaUpToDate(HttpServletRequest req) {
        long captchaCreatingTimeInMillis = (long) req.getSession().getAttribute(Constant.CAPTCHA_CREATING_TIME);
        long currentTimeInMillis = System.currentTimeMillis();

        return  (currentTimeInMillis - captchaCreatingTimeInMillis) < TIMEOUT_IN_MILLIS;
    }
}
