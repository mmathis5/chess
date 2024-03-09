package dataAccess;

import dataAccess.exceptions.UsernameExistsException;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    public void clear();
    public Boolean userExists(String username);
    public void addUser(String username, UserData user) throws SQLException, UsernameExistsException;
    public UserData getUser(String username) throws SQLException;
}
