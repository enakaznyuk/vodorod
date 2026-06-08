package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

    private static final String URL = "jdbc:postgresql://localhost:5432/store_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private JdbcUtil() {}

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        connection.setAutoCommit(true);
        return connection;
    }
}
