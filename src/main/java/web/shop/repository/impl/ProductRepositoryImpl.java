package web.shop.repository.impl;

import web.shop.bean.FilterBean;
import web.shop.entity.Category;
import web.shop.entity.Producer;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.repository.ProductRepository;
import web.shop.repository.mapper.OneColumnMapper;
import web.shop.repository.mapper.ProductMapper;
import web.shop.repository.template.JDBCTemplate;
import web.shop.util.SQLQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static web.shop.util.SQLQuery.GET_ALL_PRODUCTS;
import static web.shop.util.SQLQuery.GET_AMOUNT_OF_PRODUCTS;
import static web.shop.util.SQLQuery.GET_MAX_PRICE;
import static web.shop.util.SQLQuery.GET_MIN_PRICE;
import static web.shop.util.SQLQuery.GET_PRODUCTS;

public class ProductRepositoryImpl implements ProductRepository {

    private final JDBCTemplate jdbcTemplate;

    public ProductRepositoryImpl() {
        this.jdbcTemplate = new JDBCTemplate();
    }

    public ProductRepositoryImpl(JDBCTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean create(Product entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Product entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Product entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Product> getAll(Connection c) throws DataAccessException {
            return jdbcTemplate.queryForList(c, GET_ALL_PRODUCTS, new ProductMapper());
    }

    @Override
    public int getMaxPrice(Connection c) throws DataAccessException {
        Optional<Object> result = jdbcTemplate.queryForObject(c, GET_MAX_PRICE, new OneColumnMapper());
        return result.map(Integer.class::cast).orElse(-1);
    }

    @Override
    public int getMinPrice(Connection c) throws DataAccessException {
        Optional<Object> result = jdbcTemplate.queryForObject(c, GET_MIN_PRICE, new OneColumnMapper());
        return result.map(Integer.class::cast).orElse(-1);
    }

    @Override
    public List<Product> getPageWithProducts(Connection c, int page, int amountOfProducts) throws DataAccessException {
        return jdbcTemplate.queryForList(c, GET_PRODUCTS, new Object[]{amountOfProducts, amountOfProducts * (page - 1)}, new ProductMapper());
    }

    @Override
    public int getAmountOfProducts(Connection c) throws DataAccessException {
        Optional<Object> result = jdbcTemplate.queryForObject(c, GET_AMOUNT_OF_PRODUCTS, new OneColumnMapper());
        return result.map(o -> Math.toIntExact((Long) o)).orElse(-1);
    }

    @Override
    public List<Product> findProductsByFilter(Connection c, FilterBean filter) throws DataAccessException {
        return jdbcTemplate.queryForList(c, createFilterQuery(filter), new ProductMapper());
    }

    @Override
    public Integer getAmountOfFilteredProduct(Connection c, FilterBean filterBean) throws DataAccessException{
        StringBuilder DBQuery = new StringBuilder();
        DBQuery.append("SELECT COUNT(*) FROM product");
        filterByConditions(filterBean, DBQuery);

        Optional<Object> result = jdbcTemplate.queryForObject(c, DBQuery.toString(), new OneColumnMapper());
        return result.map(o -> Math.toIntExact((Long) o)).orElse(-1);
    }

    private String createFilterQuery(FilterBean filter) {
        StringBuilder DBQuery = new StringBuilder();
        DBQuery.append("SELECT product.id,\n" +
                "       product.name AS name,\n" +
                "       price,\n" +
                "       amount,\n" +
                "       description,\n" +
                "       c.name       AS category,\n" +
                "       p.name       AS producer,\n" +
                "       category_id,\n" +
                "       producer_id,\n" +
                "       img_path\n" +
                "FROM product");

        filterByConditions(filter, DBQuery);

        if (Objects.nonNull(filter.getSortMethod())) {
            String sortingMethod = sortingMethodToMySqlDialect(filter.getSortMethod());
            DBQuery
                    .append("\n")
                    .append(sortingMethod);
        }

        int amountOfProductsOnPage = filter.getAmountOfProductsOnPage();
        int page = amountOfProductsOnPage * (filter.getCurrentPage() - 1);
        DBQuery
                .append("\n")
                .append(String.format("LIMIT %d OFFSET %d", amountOfProductsOnPage, page));

        return DBQuery.toString();
    }

    private void filterByConditions(FilterBean filter, StringBuilder DBQuery) {
        DBQuery.append("\nJOIN category c on c.id = product.category_id");
        appendJoinCondition(filter.getCategories(), DBQuery, "c.id");

        DBQuery.append("\nJOIN producer p on p.id = product.producer_id");
        appendJoinCondition(filter.getProducers(), DBQuery, "p.id");

        String query = filter.getQuery();
        if (Objects.nonNull(query)) {
            DBQuery.append("\nWHERE product.name LIKE '%").append(query).append("%'");
        }

        DBQuery
                .append("\n")
                .append(String.format("AND price between %d and %d", filter.getMinPrice(), filter.getMaxPrice()));
    }

    private void appendJoinCondition(String[] ids, StringBuilder DBQuery, String id) {
        if (Objects.nonNull(ids)) {
            for (int i = 0; i < ids.length; i++) {
                if (i == 0) {
                    DBQuery.append(String.format(" AND (%s = ", id)).append(ids[i]);
                } else {
                    DBQuery.append(String.format(" OR %s = ", id)).append(ids[i]);
                }
            }
            DBQuery.append(")");
        }
    }

    private String sortingMethodToMySqlDialect(String sortMethod) {
        String reverse = "-reverse";
        String SQLQuery = "ORDER BY %s %s";
        if (sortMethod.contains(reverse)) {
            return String.format(SQLQuery, sortMethod.replace(reverse, ""), "DESC");
        }
        return String.format(SQLQuery, sortMethod, "ASC");
    }

    @Override
    public int getAmountOfPages(Connection c, int amountOfProductsOnPage) throws DataAccessException {
        return (int) Math.ceil((double) getAmountOfProducts(c) / amountOfProductsOnPage);
    }

    @Override
    public Optional<Product> getById(Long id, Connection c) throws SQLException {
        Optional<Product> product = Optional.empty();
        try (PreparedStatement pstmt = c.prepareStatement(SQLQuery.GET_PRODUCT_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product = Optional.of(extractProduct(rs));
            }
        }
        return product;
    }

    private Product extractProduct(ResultSet rs) throws SQLException {
        Category category = new Category().
                setId(rs.getLong("category_id"))
                .setName(rs.getString("category"));

        Producer producer = new Producer()
                .setId(rs.getLong("producer_id"))
                .setName(rs.getString("producer"));

        return new Product()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setPrice(rs.getInt("price"))
                .setAmount(rs.getInt("amount"))
                .setDescription(rs.getString("description"))
                .setCategory(category)
                .setProducer(producer)
                .setImgPath(rs.getString("img_path"));
    }
}
