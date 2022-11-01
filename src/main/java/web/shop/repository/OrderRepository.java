package web.shop.repository;

import web.shop.entity.Card;
import web.shop.entity.Order;
import web.shop.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderRepository extends DaoRepository<Order> {

    boolean createOrderWithPaymentByCard(Order order, Card card, Connection c) throws SQLException, DataAccessException;
}
