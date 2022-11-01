package web.shop.repository.impl;

import web.shop.entity.Card;
import web.shop.entity.Order;
import web.shop.entity.OrderedProduct;
import web.shop.exception.DataAccessException;
import web.shop.repository.OrderRepository;
import web.shop.repository.template.JDBCTemplate;
import web.shop.util.SQLQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static web.shop.util.SQLQuery.CREATE_ORDER;
import static web.shop.util.SQLQuery.CREATE_ORDERED_PRODUCT_INFO;
import static web.shop.util.SQLQuery.LINK_ORDER_WITH_ORDERED_PRODUCT_INFO;

public class OrderRepositoryImpl implements OrderRepository {

    private final JDBCTemplate jdbcTemplate;

    public OrderRepositoryImpl(JDBCTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getAll(Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean create(Order entity, Connection c) throws SQLException, DataAccessException {
        LocalDateTime orderTime = LocalDateTime.now();
        Timestamp timestamp = new Timestamp(orderTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli());
        Order.Status status = Order.Status.ACCEPTED;
        Object[] parameters = new Object[]{
                status.toString(),
                status.getStatusDetails(),
                timestamp,
                entity.getClient().getId(),
                entity.getPaymentMethod(),
                entity.getAddress()
        };
        Long orderId = jdbcTemplate.updateObject(c, CREATE_ORDER, parameters);
        addOrderedProductInfo(orderId, entity.getOrderedProducts(), c);

        entity.orderTime(orderTime);
        entity.setStatus(status);
        entity.setStatusDetails(status.getStatusDetails());
        entity.setId(orderId);
        return true;
    }

    @Override
    public boolean createOrderWithPaymentByCard(Order order, Card card, Connection c) throws SQLException, DataAccessException {
        create(order,c);
        jdbcTemplate.updateObject(c, SQLQuery.CREATE_CARD, new Object[] {
                order.getId(),
                card.getCvv(),
                card.getNumber(),
                card.getExpirationDate().toString()
        });
        return true;
    }

    private void addOrderedProductInfo(Long orderId, List<OrderedProduct> orderedProducts, Connection c) throws SQLException, DataAccessException {
        for (OrderedProduct p : orderedProducts) {
            Long productId = p.getProduct().getId();
            Object[] parameters = new Object[]{
                    productId,
                    p.getPrice(),
                    p.getAmount()
            };
            Long orderedProductInfoId = jdbcTemplate.updateObject(c, CREATE_ORDERED_PRODUCT_INFO, parameters);
            linkOrderWithOrderedProduct(orderId, orderedProductInfoId, c);
            updateAmountOfProductsInShop(c, p);
        }
    }

    private void updateAmountOfProductsInShop(Connection c, OrderedProduct p) throws DataAccessException {
        Integer newAmountOfProduct = p.getProduct().getAmount() - p.getAmount();
        p.getProduct().setAmount(newAmountOfProduct);
        jdbcTemplate.updateObject(c, SQLQuery.CHANGE_AMOUNT_OF_PRODUCT_IN_SHOP, new Object[]{
                newAmountOfProduct,
                p.getProduct().getId()
        });
    }

    private void linkOrderWithOrderedProduct(Long productId, Long orderedProductInfoId, Connection c) throws SQLException, DataAccessException {
        jdbcTemplate.updateObject(c, LINK_ORDER_WITH_ORDERED_PRODUCT_INFO, new Object[]{productId, orderedProductInfoId});
    }

    @Override
    public void update(Order entity, Connection c) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Order entity, Connection c) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
