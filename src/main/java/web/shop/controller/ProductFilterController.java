package web.shop.controller;

import org.apache.log4j.Logger;
import web.shop.bean.FilterBean;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.service.ProductService;
import web.shop.service.ValidatorService;
import web.shop.util.Util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static web.shop.util.Constant.AMOUNT_OF_PAGES;
import static web.shop.util.Constant.CURRENT_MAX_PRICE;
import static web.shop.util.Constant.CURRENT_MIN_PRICE;
import static web.shop.util.Constant.ERROR;
import static web.shop.util.Constant.PRODUCTS;
import static web.shop.util.Constant.PRODUCT_SERVICE;
import static web.shop.util.Constant.QUERY;
import static web.shop.util.Constant.SORT_METHOD;
import static web.shop.util.Constant.VALIDATOR_SERVICE;
import static web.shop.util.Util.getAmountOfProductsOnPage;

@WebServlet(name = "filter", value = "/product-filter")
public class ProductFilterController extends HttpServlet {

    private static final String CTGS = "ctgs";
    private static final String PRODS = "prods";

    private static final Logger LOG = Logger.getLogger(ProductFilterController.class);

    private MainPageController mainPageController;
    private ProductService productService;
    private ValidatorService validatorService;

    public ProductFilterController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public ProductFilterController() {
        this.mainPageController = new MainPageController();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        productService = (ProductService) config.getServletContext().getAttribute(PRODUCT_SERVICE);
        validatorService = (ValidatorService) config.getServletContext().getAttribute(VALIDATOR_SERVICE);
        mainPageController = new MainPageController();
        mainPageController.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            FilterBean filter = validatorService.filterValidate(req.getParameterMap(), resp);
            if (Objects.nonNull(filter)) {
                int amountOfProductsOnPage = getAmountOfProductsOnPage(req.getSession());
                String sortMethod = req.getParameter(SORT_METHOD);

                filter.setAmountOfProductsOnPage(amountOfProductsOnPage);
                filter.setCurrentPage(Util.getCurrentPage(req));
                filter.setSortMethod(sortMethod);

                List<Product> filteredProducts = productService.findProductsByFilter(filter);

                int amountOfPages = (int) Math.ceil(Math.ceil((double) productService.getAmountOfFilteredProduct(filter) / filter.getAmountOfProductsOnPage()));
                req.setAttribute(AMOUNT_OF_PAGES, amountOfPages);

                req.setAttribute(QUERY, filter.getQuery());
                req.setAttribute(SORT_METHOD, filter.getSortMethod());
                req.setAttribute(PRODUCTS, filteredProducts);
                req.setAttribute(CURRENT_MAX_PRICE, filter.getMaxPrice());
                req.setAttribute(CURRENT_MIN_PRICE, filter.getMinPrice());

                if (filteredProducts.isEmpty()) {
                    req.setAttribute(ERROR, "There's no any matches");
                    req.setAttribute(AMOUNT_OF_PAGES, 0);
                } else {
                    fillCheckboxes(req, filter.getProducers(), filter.getCategories());
                }
                mainPageController.doGet(req, resp);
            }
        } catch (DataAccessException | IOException e) {
            LOG.error(e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void fillCheckboxes(HttpServletRequest req, String[] producers, String[] categories) {
        List<Long> ctgParams = new ArrayList<>();
        List<Long> prodParams = new ArrayList<>();
        if (Objects.nonNull(categories)) {
            ctgParams.addAll(Arrays.stream(categories).mapToLong(Long::parseLong).boxed().collect(Collectors.toList()));
        }
        if (Objects.nonNull(producers)) {
            prodParams.addAll(Arrays.stream(producers).mapToLong(Long::parseLong).boxed().collect(Collectors.toList()));
        }
        req.setAttribute(CTGS, ctgParams);
        req.setAttribute(PRODS, prodParams);
    }
}

