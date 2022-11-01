package web.shop.service;

import web.shop.entity.Producer;
import web.shop.exception.DataAccessException;

import java.util.List;

public interface ProducerService {

    List<Producer> getAll() throws DataAccessException;
}
