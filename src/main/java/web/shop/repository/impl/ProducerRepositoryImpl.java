package web.shop.repository.impl;

import web.shop.entity.Producer;
import web.shop.exception.DataAccessException;
import web.shop.repository.ProducerRepository;
import web.shop.repository.mapper.ProducerMapper;
import web.shop.repository.template.JDBCTemplate;

import java.sql.Connection;
import java.util.List;

import static web.shop.util.SQLQuery.GET_ALL_PRODUCERS;

public class ProducerRepositoryImpl implements ProducerRepository {

    private final JDBCTemplate jdbcTemplate;

    public ProducerRepositoryImpl(JDBCTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Producer> getAll(Connection c) throws DataAccessException {
       return jdbcTemplate.queryForList(c, GET_ALL_PRODUCERS, new ProducerMapper());
    }

    @Override
    public boolean create(Producer entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Producer entity, Connection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Producer entity, Connection c) {
        throw new UnsupportedOperationException();
    }
}
