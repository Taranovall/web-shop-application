package web.shop.service.impl;

import web.shop.entity.Category;
import web.shop.exception.DataAccessException;
import web.shop.repository.CategoryRepository;
import web.shop.service.CategoryService;
import web.shop.service.manager.TransactionManager;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionManager transactionManager;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TransactionManager transactionManager) {
        this.categoryRepository = categoryRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Category> getAll() throws DataAccessException {
        return transactionManager.doTransaction(categoryRepository::getAll,
                "Cannot get all categories");
    }
}
