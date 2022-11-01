package web.shop.controller;

import org.apache.log4j.Logger;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.service.CategoryService;
import web.shop.service.ProducerService;
import web.shop.service.ProductService;
import web.shop.util.Constant;
import web.shop.util.Util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static web.shop.util.Constant.AMOUNT_OF_PAGES;
import static web.shop.util.Constant.CATEGORIES;
import static web.shop.util.Constant.CATEGORY_SERVICE;
import static web.shop.util.Constant.CURRENT_MAX_PRICE;
import static web.shop.util.Constant.CURRENT_MIN_PRICE;
import static web.shop.util.Constant.CURRENT_PAGE;
import static web.shop.util.Constant.MAX_PRICE;
import static web.shop.util.Constant.MIN_PRICE;
import static web.shop.util.Constant.PRODUCERS;
import static web.shop.util.Constant.PRODUCER_SERVICE;
import static web.shop.util.Constant.PRODUCTS;
import static web.shop.util.Constant.PRODUCT_SERVICE;
import static web.shop.util.Util.getCurrentPage;

@WebServlet(name = "mainPage", value = "")
public class MainPageController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(MainPageController.class);
    private ProductService productService;
    private ProducerService producerService;
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productService = (ProductService) config.getServletContext().getAttribute(PRODUCT_SERVICE);
        producerService = (ProducerService) config.getServletContext().getAttribute(PRODUCER_SERVICE);
        categoryService = (CategoryService) config.getServletContext().getAttribute(CATEGORY_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            Integer amountOfProducts = Util.getAmountOfProductsOnPage(session);
            try {
                int currentPage = getCurrentPage(req);
                req.setAttribute(CURRENT_PAGE, currentPage);
                setAmountOfPages(req, amountOfProducts);
                List<Product> allProducts = (List<Product>) req.getAttribute(PRODUCTS);
                if (Objects.isNull(allProducts)) {
                    allProducts = productService.getPageWithProducts(currentPage, amountOfProducts);
                    req.setAttribute(PRODUCTS, allProducts);
                    session.setAttribute(PRODUCTS, allProducts);
                }
                req.setAttribute(PRODUCERS, producerService.getAll());
                req.setAttribute(CATEGORIES, categoryService.getAll());
                req.setAttribute(MAX_PRICE, productService.getMaxPrice());
                req.setAttribute(MIN_PRICE, productService.getMinPrice());

                if (Objects.isNull(req.getAttribute(CURRENT_MIN_PRICE))
                        && Objects.isNull(req.getAttribute(CURRENT_MAX_PRICE))) {
                    req.setAttribute(CURRENT_MIN_PRICE, productService.getMinPrice());
                    req.setAttribute(CURRENT_MAX_PRICE, productService.getMaxPrice());
                }

                req.getRequestDispatcher(Constant.PATH_TO_MAIN_PAGE_JSP).forward(req, resp);
            } catch (DataAccessException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private void setAmountOfPages(HttpServletRequest req, Integer amountOfProducts) throws DataAccessException {
        if (Objects.isNull(req.getAttribute(AMOUNT_OF_PAGES))) {
            req.setAttribute(AMOUNT_OF_PAGES, productService.getAmountOfPages(amountOfProducts));
        }
    }
}
