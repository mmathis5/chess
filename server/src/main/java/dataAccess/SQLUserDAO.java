package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.UserData;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class SQLUserDAO implements UserDAO{
    private Connection connection;
    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("TRUNCATE userTable");
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Boolean userExists(String username){
        try{
            UserData user = getUser(username);
            return user != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addUser(String username, UserData user){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO userTable (username, hashedPassword, email) VALUES (?, ?, ?)");
            statement.setString(1, username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(user.getPassword());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.getEmail());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public UserData getUser(String username) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM userTable WHERE username=?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        String hashedPassword = resultSet.getString("password");
        String email = resultSet.getString("email");
        return new UserData(username, hashedPassword, email);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `username` VARCHAR(100),
              `password` VARCHAR(100),
              'email' VARCHAR(100)
              )
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
