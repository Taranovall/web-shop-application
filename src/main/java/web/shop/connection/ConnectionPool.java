package web.shop.connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private ConnectionPool() {
    }

    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() {
        Context context;
        Connection c = null;
        try {
            context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/mysql");
            c = ds.getConnection();
            c.setAutoCommit(false);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void rollback(Connection c) {
        try {
            if (c != null) {
                c.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
