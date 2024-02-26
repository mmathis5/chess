package dataAccess;

import java.util.Collection;
import java.util.HashMap;
import model.*;


public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> userDataHashMap = new HashMap<>();

    public MemoryUserDAO(){}

    public void clear(){
        userDataHashMap.clear();
    }
    public Boolean userExists(String username){
        //returns true if user exists
        return this.userDataHashMap.get(username) != null;
    }

    public void addUser(String username, UserData user){
        userDataHashMap.put(username, user);
    }
    public UserData getUser(String username){
        return userDataHashMap.get(username);
    }
}
