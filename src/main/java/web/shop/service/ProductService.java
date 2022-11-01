package web.shop.service;

import web.shop.bean.FilterBean;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAll() throws SQLException, DataAccessException;

    int getMinPrice() throws DataAccessException;

    int getMaxPrice() throws DataAccessException;

    List<Product> getPageWithProducts(int page, int amountOfProducts) throws DataAccessException;

    List<Product> findProductsByFilter(FilterBean filter) throws DataAccessException;

    int getAmountOfPages(int amountOfProductsOnPage) throws DataAccessException;

    int getAmountOfProducts() throws DataAccessException;

    Integer getAmountOfFilteredProduct(FilterBean filterBean) throws DataAccessException;

    Optional<Product> getById(Long productId) throws DataAccessException;
}
