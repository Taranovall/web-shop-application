package web.shop.controller;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static web.shop.util.Util.writeImgToPage;

@WebServlet(name = "imageController", value = "/images/*")
public class ImageController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ImageController.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathToFile = (System.getProperty("catalina.home") + req.getRequestURI())
                .replace("%20", " ");
        try {
            File captchaFromTomcatRoot = new File(pathToFile);
            writeImgToPage(captchaFromTomcatRoot, resp);
        } catch (FileNotFoundException e) {
            String errorMsg = String.format("File '%s' doesn't exist", pathToFile);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
