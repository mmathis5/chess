package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public UserData getUser(String username);
    public void setUser();
}
