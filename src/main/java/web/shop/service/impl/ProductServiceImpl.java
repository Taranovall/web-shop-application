package web.shop.service.impl;

import web.shop.bean.FilterBean;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.repository.ProductRepository;
import web.shop.service.ProductService;
import web.shop.service.manager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TransactionManager transactionManager;

    public ProductServiceImpl(ProductRepository productRepository, TransactionManager transactionManager) {
        this.productRepository = productRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<Product> getAll() throws DataAccessException {
        return transactionManager.doTransaction(productRepository::getAll,
                "Cannot get all products");
    }

    @Override
    public int getMinPrice() throws DataAccessException {
        return transactionManager.doTransaction(productRepository::getMinPrice,
                "Cannot get min price");
    }

    @Override
    public int getMaxPrice() throws DataAccessException {
        return transactionManager.doTransaction(productRepository::getMaxPrice,
                "Cannot get max price");
    }

    @Override
    public List<Product> getPageWithProducts(int page, int amountOfProducts) throws DataAccessException {
        String exceptionMessage = String.format("Cannot get %d page with %d products", page, amountOfProducts);
        return transactionManager.doTransaction(c -> productRepository.getPageWithProducts(c, page, amountOfProducts),
                exceptionMessage);
    }

    @Override
    public List<Product> findProductsByFilter(FilterBean filter) throws DataAccessException {
        return transactionManager.doTransaction(c -> productRepository.findProductsByFilter(c, filter),
                "Cannot filter products");
    }

    @Override
    public int getAmountOfPages(int amountOfProductsOnPage) throws DataAccessException {
        return transactionManager.doTransaction(c -> productRepository.getAmountOfPages(c, amountOfProductsOnPage),
                "Cannot get amount of pages");
    }

    @Override
    public int getAmountOfProducts() throws DataAccessException {
        return transactionManager.doTransaction(productRepository::getAmountOfProducts,
                "Cannot get amount of products");
    }

    @Override
    public Integer getAmountOfFilteredProduct(FilterBean filterBean) throws DataAccessException {
        return transactionManager.doTransaction(c -> productRepository.getAmountOfFilteredProduct(c, filterBean),
                "Cannot get amount of filtered products");
    }

    @Override
    public Optional<Product> getById(Long productId) throws DataAccessException {
        return transactionManager.doTransaction(c -> productRepository.getById(productId, c),
                "Cannot get product by id"
        );
    }
}
