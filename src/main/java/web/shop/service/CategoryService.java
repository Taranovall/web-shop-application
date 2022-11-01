package web.shop.service;

import web.shop.entity.Category;
import web.shop.exception.DataAccessException;

import java.util.List;

public interface CategoryService {

    List<Category> getAll() throws DataAccessException;
}
