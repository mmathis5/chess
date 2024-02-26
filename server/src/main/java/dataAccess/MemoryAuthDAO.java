package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private int nextAuthToken = 1;
    final private HashMap<String, AuthData> authDataHashMap = new HashMap<>();

    public MemoryAuthDAO(){}
    public void clear(){
        authDataHashMap.clear();
    }

    public String generateAuthToken(){
        String authToken = String.valueOf(nextAuthToken);
        nextAuthToken++;
        return authToken;
    }
    public void addAuthData(String authToken, AuthData authData){
        authDataHashMap.put(authToken, authData);
    }
    public AuthData getAuth(String authToken){
        return authDataHashMap.get(authToken);
    }

    public void deleteAuth(String authToken){
        authDataHashMap.remove(authToken);
    }
}
