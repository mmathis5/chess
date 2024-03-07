package dataAccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    public void clear();
    public Boolean userExists(String username);
    public void addUser(String username, UserData user);
    public UserData getUser(String username) throws SQLException;
}
