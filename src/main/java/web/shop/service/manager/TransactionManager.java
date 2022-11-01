package web.shop.service.manager;

import web.shop.exception.DataAccessException;
import web.shop.exception.IncorrectAmountOfParametersException;
import web.shop.service.TransactionOperation;

import java.sql.Connection;
import java.sql.SQLException;

import static web.shop.connection.ConnectionPool.close;
import static web.shop.connection.ConnectionPool.getInstance;
import static web.shop.connection.ConnectionPool.rollback;

public class TransactionManager {

    /**
     * Method wrapper for transaction.
     * It gets instance of connection from connection pool and execute operation passed in functional interface.
     * Makes commit if there's no error otherwise rollbacks all changes and throw exception with message.
     * @param operation functional interface
     * @param exceptionMessage throws exception with this message if there's any error
     * @return result of method passed to the functional interface
     * @param <T> depends on method passed from repository to the functional interface
     * @throws web.shop.exception.DataAccessException
     */
    public <T> T doTransaction(TransactionOperation<T> operation, String exceptionMessage) throws DataAccessException {
        T result;
        Connection c = null;
        try {
            c = getInstance().getConnection();
            result = operation.doTransaction(c);
            c.commit();
        } catch (DataAccessException | SQLException | IncorrectAmountOfParametersException e) {
            rollback(c);
            throw new DataAccessException(exceptionMessage, e);
        } finally {
            close(c);
        }
        return result;
    }
}
