package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public Boolean userExists(String username);
    public void addUser(String username, UserData user);
    public UserData getUser(String username);
}
