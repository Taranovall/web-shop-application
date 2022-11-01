package web.shop.repository.impl;

import web.shop.entity.Category;
import web.shop.exception.DataAccessException;
import web.shop.repository.CategoryRepository;
import web.shop.repository.mapper.CategoryMapper;
import web.shop.repository.template.JDBCTemplate;

import java.sql.Connection;
import java.util.List;

import static web.shop.util.SQLQuery.GET_ALL_CATEGORIES;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final JDBCTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JDBCTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CategoryRepositoryImpl() {
        this.jdbcTemplate = new JDBCTemplate();
    }

    @Override
    public List<Category> getAll(Connection c) throws DataAccessException {
        return jdbcTemplate.queryForList(c, GET_ALL_CATEGORIES, new CategoryMapper());
    }

    @Override
    public boolean create(Category entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Category entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Category entity, Connection c) {
        throw new UnsupportedOperationException();
    }
}
