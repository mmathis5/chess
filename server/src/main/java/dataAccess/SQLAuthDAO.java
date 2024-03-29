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
            PreparedStatement statement = connection.prepareStatement("DELETE FROM auths");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String generateAuthToken(){
        return String.valueOf(Math.random());
    }
    public void addAuthData(String authToken, AuthData authData) throws SQLException, DataAccessException {
        try {
            if (getAuth(authToken) != null){
                throw new DataAccessException("Something went wrong");
            }
            // Create a PreparedStatement with the INSERT statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)");

            // Set values for the placeholders in the SQL statement
            statement.setString(1, authToken);
            statement.setString(2, authData.getUsername());

            // Execute the INSERT statement
            statement.execute();

            // Close the statement
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public AuthData getAuth(String authToken) throws SQLException {
        AuthData authData = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM auths WHERE authToken=?");
        statement.setString(1, authToken);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String username = resultSet.getString("username");
            authData = new AuthData(authToken, username);
        }
        return authData;
    }

    public void deleteAuth(String authToken) throws SQLException {
        try{
            if (getAuth(authToken) == null){
                throw new DataAccessException("Something went wrong");
            }
            PreparedStatement statement = connection.prepareStatement("DELETE FROM auths WHERE authToken=?");
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new SQLException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
