package web.shop.service;

import web.shop.entity.Card;
import web.shop.entity.Order;
import web.shop.exception.DataAccessException;

public interface OrderService {

    boolean createOrderWithPaymentByCash(Order order) throws DataAccessException;

    boolean createOrderWithPaymentByCard(Order order, Card card) throws DataAccessException;
}
