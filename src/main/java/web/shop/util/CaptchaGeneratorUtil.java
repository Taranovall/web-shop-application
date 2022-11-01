package web.shop.util;


import web.shop.captcha.CaptchaHandler;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static web.shop.util.Constant.CAPTCHA_CREATING_TIME;
import static web.shop.util.Constant.CAPTCHA_HANDLER;
import static web.shop.util.Constant.COOKIE_WITH_CAPTCHA_ID;


public class CaptchaGeneratorUtil {

    private CaptchaGeneratorUtil() {
    }

    /**
     * Generates image with captcha and put it into a catalina.base/images/captcha
     * @param req
     * @param resp
     * @throws java.io.IOException
     */
    public static void generate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String captchaText = generateFiveDigitNumber();
        ((CaptchaHandler) session.getServletContext().getAttribute(CAPTCHA_HANDLER)).save(req, captchaText);

        File imgWithCaptcha = createImageWithSpecifiedText(captchaText);
        session.setAttribute(CAPTCHA_CREATING_TIME, System.currentTimeMillis());
        createCaptchaImgInTomcatRoot(session.getId(), imgWithCaptcha);
        addCookie(req,resp);
    }

    private static void addCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = (Cookie) req.getAttribute(COOKIE_WITH_CAPTCHA_ID);
        if (Objects.nonNull(cookie)) {
            resp.addCookie(cookie);
        }
    }

    private static void createCaptchaImgInTomcatRoot(String sessionID, File imgWithCaptcha) {
        String fileName = String.format("/Captcha_%s.jpg", sessionID);
        File captchaImgDir = new File(System.getProperty("catalina.base") + "/images/captcha/");
        captchaImgDir.mkdirs();
        File newCaptchaImg = new File(captchaImgDir + fileName);
        imgWithCaptcha.renameTo(newCaptchaImg);
        newCaptchaImg.deleteOnExit();
    }


    private static File createImageWithSpecifiedText(String text) throws IOException {
        File captcha = File.createTempFile("captcha", ".jpg");
        BufferedImage image = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
        Font font = new Font("Weltron Urban Font", Font.BOLD, 50);

        Graphics g = image.getGraphics();
        FontMetrics metrics = g.getFontMetrics(font);
        int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, positionX, positionY);

        ImageIO.write(image, "jpg", captcha);

        return captcha;
    }

    private static String generateFiveDigitNumber() {
        return String.valueOf(ThreadLocalRandom
                .current()
                .nextInt(10000, 100000));
    }
}
