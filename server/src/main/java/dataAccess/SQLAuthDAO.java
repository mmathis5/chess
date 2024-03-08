package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    private Connection connection;
    private int nextAuthToken = 1;

    public SQLAuthDAO() throws DataAccessException {
        //configureDatabase();
        this.connection = DatabaseManager.getConnection();
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("DROP table authTable");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String generateAuthToken(){
        String authToken = String.valueOf(nextAuthToken);
        nextAuthToken++;
        return authToken;
    }
    public void addAuthData(String authToken, AuthData authData){
        try {
            // Create a PreparedStatement with the INSERT statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO authTable (authToken, username) VALUES (?, ?)");

            // Set values for the placeholders in the SQL statement
            statement.setString(1, authToken);
            statement.setString(2, authData.getUsername());

            // Execute the INSERT statement
            statement.executeUpdate();

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public AuthData getAuth(String authToken) throws SQLException {
        AuthData authData = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM authTable WHERE authToken=?");
        statement.setString(1, authToken);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String username = resultSet.getString("username");
            authData = new AuthData(authToken, username);
        }
        return authData;
    }

    public void deleteAuth(String authToken){
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM authTable WHERE authToken=?");
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

//    private void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            DatabaseManager.createAuthTable(conn);
//        } catch (SQLException ex) {
//            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }
}
