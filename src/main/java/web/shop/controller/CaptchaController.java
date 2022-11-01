package web.shop.controller;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static web.shop.util.Constant.FILE_NOT_EXIST;
import static web.shop.util.Util.writeImgToPage;

@WebServlet(name = "captchaGenerator", value = "/captcha/*")
public class CaptchaController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CaptchaController.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filePath = getFileName(req.getRequestURI());
        try {
            String pathToTomcatImgDir = System.getProperty("catalina.base") + "/images/captcha/";

            File captchaFromTomcatRoot = new File(pathToTomcatImgDir + filePath);
            writeImgToPage(captchaFromTomcatRoot, resp);
        } catch (FileNotFoundException e) {
            String errorMsg = String.format(FILE_NOT_EXIST, filePath);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
            LOG.error(errorMsg, e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String getFileName(String URI) {
        String filePath = null;
        Pattern pattern = Pattern.compile("^\\/captcha\\/(.+)$");
        Matcher matcher = pattern.matcher(URI);
        if (matcher.find()) {
            filePath = matcher.group(1);
        }
        return filePath;
    }
}
