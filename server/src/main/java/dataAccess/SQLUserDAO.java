package dataAccess;

import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.UsernameExistsException;
import model.UserData;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class SQLUserDAO implements UserDAO{
    private Connection connection;
    public SQLUserDAO() throws DataAccessException {
        this.connection = DatabaseManager.getConnection();
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users");
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
    public void addUser(String username, UserData user) throws SQLException, UsernameExistsException {
        try {
            if (userExists(username)){
                throw new UsernameExistsException("this username is already taken");
            }
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
            statement.setString(1, username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(user.getPassword());
            statement.setString(2, hashedPassword);
            statement.setString(3, user.getEmail());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }catch (UsernameExistsException e){
            throw e;
        }
    }
    public UserData getUser(String username) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=?");

        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        UserData user = null;
        if (resultSet.next()){
            String hashedPassword = resultSet.getString("password");
            String email = resultSet.getString("email");
            user = new UserData(username, hashedPassword, email);
        }
        return user;
    }

    public String getUsername(String authToken) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM auths WHERE authToken=?");

        statement.setString(1, authToken);
        ResultSet resultSet = statement.executeQuery();
        String username = null;
        if (resultSet.next()){
            username = resultSet.getString("username");
        }
        return username;
    }
}
