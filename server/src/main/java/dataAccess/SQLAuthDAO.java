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

    }
    public AuthData getAuth(String authToken){

    }

    public void deleteAuth(String authToken){

    }
}
