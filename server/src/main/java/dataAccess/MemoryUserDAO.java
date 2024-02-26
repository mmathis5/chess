package dataAccess;

import java.util.Collection;
import java.util.HashMap;
import model.*;


public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, Collection<String>> userDataHashMap = new HashMap<>();

    public MemoryUserDAO(){}

    public void clear(){
        userDataHashMap.clear();
    }
    public UserData getUser(String username){
        return
    }
    public void setUser(){


    }
}
