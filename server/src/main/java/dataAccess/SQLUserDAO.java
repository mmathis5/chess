package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;


public class SQLUserDAO implements UserDAO{
    private Connection connection;
    public SQLUserDAO(){try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "username", "password");
      } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if (getUser(username) != SQLException){
            return true;
        }
        return false;
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
    public UserData getUser(String username){
        try{
            PreparedStatement statement = connection.prepareStatement("GET FROM userTable WHERE username=?");
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
        //idk how to return this
    }
}
