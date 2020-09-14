package ch.qos.logback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HSQLDBConnection {
    private static final Logger LOGGER = Logger.getLogger(HSQLDBConnection.class.getName());
    public static String defaultHost = "localhost";
    public static String defaultDb = "logging";
    public static String defaultDbUser = "SA";
    public static String defaultDbPassword = "";

    /**
     * creates a HSQLDB connection with the default parameters
     * @return  HSQLDB connection to the default database
     */
    public static Connection getConnection() {
        return getConnection(defaultHost, defaultDb, defaultDbUser, defaultDbPassword);
    }

    // TODO: add documentation
    public static Connection getConnection(String host, String db, String user, String password) {
        Connection con = null;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            LOGGER.log(Level.FINE, "HSQLDB JDBCDriver Loaded");
            con = DriverManager.getConnection(
                "jdbc:hsqldb:hsql://" + host + "/" + db,
                user,
                password
            );
            LOGGER.log(Level.FINE, "HSQLDB Connection Created");
        } catch (ClassNotFoundException e) {
            // TODO: create custom exception for when DB connection could not be created?
            LOGGER.log(Level.SEVERE, "HSQLDB JDBC driver missing");
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not execute SQL statement");
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return con;
    }
}
