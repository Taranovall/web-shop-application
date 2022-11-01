package web.shop.service.impl;

import web.shop.entity.Producer;
import web.shop.exception.DataAccessException;
import web.shop.repository.ProducerRepository;
import web.shop.service.ProducerService;
import web.shop.service.manager.TransactionManager;

import java.util.List;

public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository producerRepository;
    private final TransactionManager transactionManager;

    public ProducerServiceImpl(ProducerRepository producerRepository, TransactionManager transactionManager) {
        this.producerRepository = producerRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Producer> getAll() throws DataAccessException {
       return transactionManager.doTransaction(producerRepository::getAll,
               "Cannot get all producers");
    }
}
