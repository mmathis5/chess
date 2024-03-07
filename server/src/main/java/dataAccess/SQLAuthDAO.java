package dataAccess;

import model.AuthData;

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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clear(){

    }
    public String generateAuthToken(){
        String authToken = String.valueOf(nextAuthToken);
        nextAuthToken++;
        return authToken;
    }
    public void addAuthData(String authToken, AuthData authData){
        try {
            // Create a PreparedStatement with the INSERT statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auth_table (auth_token, username) VALUES (?, ?)");

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
    public AuthData getAuth(String authToken){

    }

    public void deleteAuth(String authToken){

    }
}
