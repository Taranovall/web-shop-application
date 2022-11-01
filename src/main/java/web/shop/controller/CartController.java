package web.shop.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import web.shop.dto.CartDTO;
import web.shop.dto.ProductDTO;
import web.shop.entity.Cart;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.exception.ProductNotFoundException;
import web.shop.service.ProductService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static web.shop.util.Constant.AMOUNT;
import static web.shop.util.Constant.CART;
import static web.shop.util.Constant.PRODUCT;
import static web.shop.util.Constant.PRODUCT_ID;
import static web.shop.util.Constant.PRODUCT_SERVICE;

@WebServlet(name = "cartController", value = "/cart/*")
public class CartController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(CartController.class);
    private ProductService productService;

    @Override
    public void init(ServletConfig config) {
        productService = (ProductService) config.getServletContext().getAttribute(PRODUCT_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            Cart cart = getOrCreateCart(session);
            try {
                Long productId = Long.valueOf(req.getRequestURI().split("/")[2]);
                Optional<Product> optionalProduct = productService.getById(productId);

                if (optionalProduct.isPresent()) {
                    cart.putProduct(optionalProduct.get());
                    String json = addProductToCartJsonResult(cart, optionalProduct.get());
                    writeJson(resp, json);
                } else {
                    String message = String.format("Product with id %d doesn't exist", productId);
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
                }
            } catch (DataAccessException | NumberFormatException e) {
                LOG.error(e.getMessage(), e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            Cart cart = (Cart) session.getAttribute(CART);
            Long productId = Long.valueOf(req.getParameter(PRODUCT_ID));
            try {
                cart.removeProduct(productId);
                writeJson(resp, new Gson().toJson(cartToJson(cart)));
            } catch (ProductNotFoundException e) {
                LOG.error(e.getMessage(), e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        synchronized (session) {
            Long productId = Long.valueOf(req.getParameter(PRODUCT_ID));
            Integer amount = Integer.valueOf(req.getParameter(AMOUNT));
            Cart cart = (Cart) session.getAttribute(CART);
            try {
                cart.changeAmountOfProduct(productId, amount);
                writeJson(resp, new Gson().toJson(cartToJson(cart)));
            } catch (ProductNotFoundException e) {
                LOG.error(e.getMessage(), e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    private String addProductToCartJsonResult(Cart cart, Product product) {
        ProductDTO productDTO = new ProductDTO()
                .setName(product.getName())
                .setPath(product.getImgPath())
                .setPrice(product.getPrice())
                .setAvailableAmount(product.getAmount())
                .setCurrentAmount(cart.getProducts().get(product));

        CartDTO cartDTO = cartToJson(cart);

        Map<String, Object> mapToBeConvertedToJson = new HashMap<>();
        mapToBeConvertedToJson.put(PRODUCT, productDTO);
        mapToBeConvertedToJson.put(CART, cartDTO);

        return new Gson().toJson(mapToBeConvertedToJson);
    }

    private CartDTO cartToJson(Cart cart) {
        return new CartDTO()
                .setTotalPrice(cart.getTotalPrice())
                .setAmountOfProducts(cart.getAmountOfProducts());
    }

    private void writeJson(HttpServletResponse resp, String json) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("plain/text");
        try (PrintWriter w = resp.getWriter()) {
            w.write(json);
        }
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART);
        if (Objects.isNull(cart)) {
            cart = new Cart();
            session.setAttribute(CART, cart);
        }
        return cart;
    }
}
