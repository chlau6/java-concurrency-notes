package listing3p10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ThreadConfinement  {
    private static final String DB_URL = "";
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        public Connection initialValue() {
            try {
                return DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}

/*
A single-threaded application might maintain a global database connection that is initialized at startup
to avoid having to pass a Connection to every method.

Since JDBC connections may not be thread-safe,
a multithreaded application that uses a global connection without additional coordination is not thread-safe either.

By using a ThreadLocal to store the JDBC connection, as in ConnectionHolder, each thread will have its own connection.
 */
