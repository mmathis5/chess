package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private int nextAuthToken = 1;
    final private HashMap<String, Collection<String>> authDataHashMap = new HashMap<>();

    public MemoryAuthDAO(){}
    public void clear(){
        authDataHashMap.clear();
    }

    public String generateAuthToken(){
        String authToken = String.valueOf(nextAuthToken);
        nextAuthToken++;
        return authToken;
    }
    public AuthData getAuth(AuthData authData){

    }

    public void deleteAuth(AuthData authData){

    }
}
