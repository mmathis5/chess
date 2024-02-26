package dataAccess;

import model.*;
import service.*;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    private int nextAuthToken = 1;
    //authToken should be the key, username the value
    final private HashMap<Integer, AuthData> AuthDataHashMap = new HashMap<>();
    final private HashMap<Integer, GameData> GameDataHashMap = new HashMap<>();
    final private HashMap<String, Collection<String>> UserDataHashMap = new HashMap<>();

    public MemoryDataAccess(){
    }
    public void clear(){
        AuthDataHashMap.clear();
        GameDataHashMap.clear();
        UserDataHashMap.clear();
    }

    public UserData register(String username, String password, String email){
        UserData newUser = new UserData(username, password, email);
    }
    public AuthData login(String username, String password) throws DataAccessException {
        Collection<String> UserEntry = UserDataHashMap.get(username);
        //I want this to link to the function in my user service I think
        Boolean foundPassword = UserEntry.contains(password);
        if (!foundPassword){
            throw new DataAccessException("Your username and password don't match");
        }
            AuthData newAuthData = new AuthData(nextAuthToken, username);
            //insert the newAuthData into the hashmap
            AuthDataHashMap.put(nextAuthToken, newAuthData);
            nextAuthToken ++;
            return newAuthData;
    }
    public void logout(AuthData authData){
        int authToken = authData.getAuthToken();
        AuthDataHashMap.remove(authToken);
    }
    //Almost positive this is wildly false
    public Collection<GameData> listGames(){
        return GameDataHashMap.values();
    }
    public GameData createGame(String gameName){

    }

    public GameData joinGame(String playerColor, String gameID) {

    }
}
