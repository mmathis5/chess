package dataAccess;

import dataAccess.exceptions.DataAccessException;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;

            var conn = DriverManager.getConnection(connectionUrl, user, password);

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void createUserTable(Connection conn) throws SQLException {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS userTable (" +
                "username VARCHAR(100) PRIMARY KEY," +
                "password VARCHAR(100) NOT NULL," +
                "email VARCHAR(100)" +
                ")";
        try (var preparedStatement = conn.prepareStatement(createUserTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }
    static void createAuthTable(Connection conn) throws SQLException {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS authTable (" +
                "authToken VARCHAR(100) PRIMARY KEY," +
                "username VARCHAR(100) NOT NULL" +
                ")";
        try (var preparedStatement = conn.prepareStatement(createUserTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    static void createGameTable(Connection conn) throws SQLException {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS gameTable (" +
                "gameName VARCHAR(100)," +
                "gameID INT," +
                "whiteUsername VARCHAR(100)," +
                "blackUsername VARCHAR(100)," +
                "game BLOB" +
                ")";
        try (var preparedStatement = conn.prepareStatement(createUserTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }
    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
