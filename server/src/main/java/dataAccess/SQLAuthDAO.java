package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    private Connection connection;
    private int nextAuthToken = 1;

    public SQLAuthDAO() {
        // Initialize the database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chess", "root", "password");
            DatabaseManager.createDatabase();
            DatabaseManager.createAuthTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("TRUNCATE authTable");
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
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM authTable WHERE authToken=?");
        statement.setString(1, authToken);
        ResultSet resultSet = statement.executeQuery();
        String username = resultSet.getString("username");
        return new AuthData(authToken, username);
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
}
