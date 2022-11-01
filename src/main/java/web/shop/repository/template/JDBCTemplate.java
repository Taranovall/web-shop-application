package web.shop.repository.template;

import web.shop.exception.DataAccessException;
import web.shop.exception.IncorrectAmountOfParametersException;
import web.shop.repository.mapper.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class JDBCTemplate {

    /**
     * @param connection
     * @param query      SQL query
     * @param params     parameters for prepared statement
     * @param mapper     object mapper
     * @param <T>
     * @return optional of object
     */
    public <T> Optional<T> queryForObject(Connection connection, String query, Object[] params, RowMapper<T> mapper) throws DataAccessException {
        Optional<T> result = Optional.empty();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            validateAmountOfParameters(query, params);
            fillPreparedStatementWithParameters(params, pstmt);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = Optional.ofNullable(mapper.mapRow(rs));
            }
        } catch (IncorrectAmountOfParametersException | SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return result;
    }


    /**
     * @param connection
     * @param query      SQL query
     * @param mapper     object mapper
     * @param <T>
     * @return optional of object
     */
    public <T> Optional<T> queryForObject(Connection connection, String query, RowMapper<T> mapper) throws DataAccessException {
        Optional<T> result = Optional.empty();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                result = Optional.ofNullable(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param connection
     * @param query      SQL query
     * @param params     parameters for prepared statement
     * @param mapper     object mapper
     * @param <T>
     * @return list of objects T from database depends on sql query
     */
    public <T> List<T> queryForList(Connection connection, String query, Object[] params, RowMapper<T> mapper) throws DataAccessException {
        List<T> list = new LinkedList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            validateAmountOfParameters(query, params);
            fillPreparedStatementWithParameters(params, pstmt);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapper.mapRow(rs));
            }
        } catch (SQLException | IncorrectAmountOfParametersException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return list;
    }

    /**
     * @param connection
     * @param query      SQL query
     * @param mapper     object mapper
     * @param <T>
     * @return list of objects T from database depends on sql query
     */
    public <T> List<T> queryForList(Connection connection, String query, RowMapper<T> mapper) throws DataAccessException{
        List<T> list = new LinkedList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(mapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return list;
    }

    /**
     * @param connection
     * @param query      SQL query
     * @param params     parameters of prepared statement
     * @return ID of updated/created row or -1 if row was deleted
     */
    public Long updateObject(Connection connection, String query, Object[] params) throws DataAccessException {
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            validateAmountOfParameters(query, params);
            fillPreparedStatementWithParameters(params, pstmt);
            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (IncorrectAmountOfParametersException | SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return -1L;
    }

    /**
     * fill every value with the designated parameter
     * @param params
     * @param pstmt
     * @throws java.sql.SQLException
     */
    private void fillPreparedStatementWithParameters(Object[] params, PreparedStatement pstmt) throws SQLException {
        int columnId;
        for (int i = 0; i < params.length; i++) {
            columnId = i + 1;
            pstmt.setObject(columnId, params[i]);
        }
    }

    /**
     *
     * @param query
     * @param params
     * @throws web.shop.exception.IncorrectAmountOfParametersException if amount of parameters in query aren't equal amount of parameters in params
     */
    private void validateAmountOfParameters(String query, Object[] params) throws IncorrectAmountOfParametersException {
        int amountOfParams = (int) query.chars().filter(ch -> ch == '?').count();
        if (amountOfParams != params.length) {
            String message = String.format("Amount of parameters wanted in sql query: %d, amount of passed parameters %d",
                    amountOfParams, params.length);
            throw new IncorrectAmountOfParametersException(message);
        }
    }
}
