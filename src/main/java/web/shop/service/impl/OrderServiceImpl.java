package web.shop.service.impl;

import web.shop.entity.Card;
import web.shop.entity.Order;
import web.shop.exception.DataAccessException;
import web.shop.repository.OrderRepository;
import web.shop.service.OrderService;
import web.shop.service.manager.TransactionManager;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TransactionManager transactionManager;

    public OrderServiceImpl(OrderRepository orderRepository, TransactionManager transactionManager) {
        this.orderRepository = orderRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public boolean createOrderWithPaymentByCash(Order order) throws DataAccessException {
        return transactionManager.doTransaction(c -> orderRepository.create(order, c),
                "Cannot make an order");
    }

    @Override
    public boolean createOrderWithPaymentByCard(Order order, Card card) throws DataAccessException {
        return transactionManager.doTransaction(c -> orderRepository.createOrderWithPaymentByCard(order, card, c),
                "Cannot make an order");
    }
}
