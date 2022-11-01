package web.shop.controller;

import web.shop.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static web.shop.util.Constant.AMOUNT_OF_PRODUCTS;

@WebServlet(name = "amountOfProducts", value = "/amount_of_products")
public class AmountOfProductsController extends HttpServlet {

    private static final String PRODUCT_AMOUNT_PARAMETER = "product-amount";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            int amountOfProducts = Integer.parseInt(req.getParameter(PRODUCT_AMOUNT_PARAMETER));
            session.setAttribute(AMOUNT_OF_PRODUCTS, amountOfProducts);
            String previousURL = Util.getPreviousURL(req).replaceAll("[?&]?page=(\\d+)", "");
            resp.sendRedirect(previousURL);
        }
    }
}
