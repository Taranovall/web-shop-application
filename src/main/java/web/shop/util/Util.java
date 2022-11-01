package web.shop.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static web.shop.util.Constant.AMOUNT_OF_PRODUCTS;
import static web.shop.util.Constant.PAGE;

public class Util {

    /**
     * Writes image from file to servlet
     *
     * @param img
     * @param resp
     * @throws java.io.IOException
     */
    public static void writeImgToPage(File img, HttpServletResponse resp) throws IOException {
        try (FileInputStream in = new FileInputStream(img);
             OutputStream out = resp.getOutputStream()) {
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }
    }

    /**
     * @param req
     * @return previous URL
     */
    public static String getPreviousURL(HttpServletRequest req) {
        String baseURL = String.format("%s://%s:%s", req.getScheme(), req.getServerName(), req.getServerPort());
        return req.getHeader("referer").replace(baseURL, "");
    }

    /**
     * @param session
     * @return amount of products displaying on product page
     */
    public static Integer getAmountOfProductsOnPage(HttpSession session) {
        Integer amountOfProducts = (Integer) session.getAttribute(AMOUNT_OF_PRODUCTS);
        if (Objects.isNull(amountOfProducts)) {
            session.setAttribute(AMOUNT_OF_PRODUCTS, Constant.DEFAULT_AMOUNT_OF_PRODUCTS);
            return Constant.DEFAULT_AMOUNT_OF_PRODUCTS;
        }
        return amountOfProducts;
    }

    /**
     * @param req
     * @return number of current page
     */
    public static int getCurrentPage(HttpServletRequest req) {
        String currentPage = req.getParameter(PAGE);
        if (Objects.isNull(currentPage)) {
            return 1;
        }
        return Integer.parseInt(currentPage);
    }
}
