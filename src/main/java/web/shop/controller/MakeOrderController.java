package web.shop.controller;


import org.apache.log4j.Logger;
import web.shop.entity.Card;
import web.shop.entity.Cart;
import web.shop.entity.Order;
import web.shop.entity.OrderedProduct;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.service.OrderService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static web.shop.util.Constant.ADDRESS;
import static web.shop.util.Constant.CARD;
import static web.shop.util.Constant.CART;
import static web.shop.util.Constant.CASH;
import static web.shop.util.Constant.CVV;
import static web.shop.util.Constant.DELIVERY_METHOD;
import static web.shop.util.Constant.ERROR;
import static web.shop.util.Constant.EXPIRATION_DATE;
import static web.shop.util.Constant.EXPIRATION_DATE_PATTERN;
import static web.shop.util.Constant.ORDER_HAS_BEEN_MADE;
import static web.shop.util.Constant.ORDER_SERVICE;
import static web.shop.util.Constant.PATH_TO_ORDER_PAGE;
import static web.shop.util.Constant.PAYMENT;
import static web.shop.util.Constant.UNAUTHORIZED_MESSAGE;
import static web.shop.util.Constant.USER;

@WebServlet(name = "makeOrder", value = "/make-order")
public class MakeOrderController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(MakeOrderController.class);
    private OrderService orderService;
    private MainPageController mainPageController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.orderService = (OrderService) config.getServletContext().getAttribute(ORDER_SERVICE);
        this.mainPageController = new MainPageController();
        this.mainPageController.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (Objects.isNull(session.getAttribute(USER))) {
            req.setAttribute(ERROR, UNAUTHORIZED_MESSAGE);
        }
        req.getRequestDispatcher(PATH_TO_ORDER_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User client = (User) session.getAttribute(USER);
        try {
            if (Objects.nonNull(client)) {
                Cart cart = (Cart) session.getAttribute(CART);
                Order order = extractOrderFromRequest(req)
                        .setClient(client)
                        .setOrderedProducts(convertCartToOrderedProducts(cart));
                if (order.getPaymentMethod().equals(CARD)) {
                    orderService.createOrderWithPaymentByCard(order, extractCardFromRequest(req));
                } else if (order.getPaymentMethod().equals(CASH)) {
                    orderService.createOrderWithPaymentByCash(order);
                }
                session.setAttribute(CART, null);
                req.setAttribute(ORDER_HAS_BEEN_MADE, String.format("Order with ID %d has been successfully made", order.getId()));
                mainPageController.doGet(req, resp);
            }
        } catch (DataAccessException e) {
            LOG.error(e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Order extractOrderFromRequest(HttpServletRequest req) {
        String address = req.getParameter(ADDRESS);
        String deliveryMethod = req.getParameter(DELIVERY_METHOD);
        String paymentMethod = req.getParameter(PAYMENT);
        return new Order(paymentMethod, deliveryMethod, address);
    }

    private Card extractCardFromRequest(HttpServletRequest req) {
        YearMonth expirationDate = YearMonth.parse(
                req.getParameter(EXPIRATION_DATE),
                DateTimeFormatter.ofPattern(EXPIRATION_DATE_PATTERN));
        Integer cvv = Integer.valueOf(req.getParameter(CVV));
        String cardNumber = req.getParameter(CARD);
        return new Card()
                .setCvv(cvv)
                .setNumber(cardNumber)
                .setExpirationDate(expirationDate);
    }

    private List<OrderedProduct> convertCartToOrderedProducts(Cart cart) {
        return cart
                .getProducts()
                .entrySet()
                .stream()
                .map(e -> new OrderedProduct(e.getKey(), e.getKey().getPrice(), e.getValue()))
                .collect(Collectors.toList());
    }
}
