package dataAccess;

import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    private int nextAuthToken = 1;
    //authToken should be the key, username the value
    final private HashMap<Integer, AuthData> AuthDataHashMap = new HashMap<>();
    final private HashMap<Integer, GameData> GameDataHashMap = new HashMap<>();
    final private HashMap<Integer, UserData> UserDataHashMap = new HashMap<>();

    public void clear(){
        AuthDataHashMap.clear();
        GameDataHashMap.clear();
        UserDataHashMap.clear();
    }

    public UserData register(String username, String password, String email){

    }
    public UserData login(String username, String password, String email){
        
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
