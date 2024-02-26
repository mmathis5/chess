package dataAccess;

import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> gameDataHashMap = new HashMap<>();
    private int nextGameID = 1;

    public MemoryGameDAO(){}
    public void clear() {
        gameDataHashMap.clear();
    }
    public Integer createGame(String authToken, String gameName){
        //get gameID
        int gameID = nextGameID;
        //increment the gameID for the next usage
        nextGameID ++;
        //create new GameData instance
        GameData newGame = new GameData(gameName, gameID);
        //add it to the hashMap using the authToken as the key
        gameDataHashMap.put(gameID, newGame);
        return gameID;
    }
    public ArrayList<GameData> getGamesList(){
        ArrayList<GameData> gameList = new ArrayList<GameData>();
        //iterate through the hash map and add all the GameData instances
        for (Map.Entry<Integer, GameData> entry : gameDataHashMap.entrySet()) {
            GameData game = entry.getValue();
            gameList.add(game);
        }
        return gameList;
    }
    public GameData getGame(Integer gameID){return gameDataHashMap.get(gameID);}

//    public void updateGame(GameData gameData){
//
//    }

}
