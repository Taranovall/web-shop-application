package web.shop.service;

import web.shop.exception.DataAccessException;
import web.shop.exception.IncorrectAmountOfParametersException;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface TransactionOperation<T> {

    T doTransaction(Connection c) throws DataAccessException, SQLException, IncorrectAmountOfParametersException;
}
