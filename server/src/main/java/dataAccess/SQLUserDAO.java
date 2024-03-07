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

    }
    void storeUserPassword(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }


    public Boolean userExists(String username){

    }
    public void addUser(String username, UserData user){

    }
    public UserData getUser(String username){

    }
}
