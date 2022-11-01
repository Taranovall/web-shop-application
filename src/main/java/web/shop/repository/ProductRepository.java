package web.shop.repository;

import web.shop.bean.FilterBean;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.exception.IncorrectAmountOfParametersException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends DaoRepository<Product> {

    int getMaxPrice(Connection c) throws SQLException, DataAccessException;

    int getMinPrice(Connection c) throws SQLException, DataAccessException;

    List<Product> getPageWithProducts(Connection c, int page, int amountOfProducts) throws SQLException, IncorrectAmountOfParametersException, DataAccessException;

    List<Product> findProductsByFilter(Connection c, FilterBean filter) throws SQLException, DataAccessException;

    int getAmountOfPages(Connection c, int amountOfProductsOnPage) throws SQLException, DataAccessException;

    int getAmountOfProducts(Connection c) throws SQLException, DataAccessException;

    Integer getAmountOfFilteredProduct(Connection c, FilterBean filterBean) throws SQLException, DataAccessException;

    Optional<Product> getById(Long id, Connection c) throws SQLException;
}
