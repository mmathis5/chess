package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> gameDataHashMap = new HashMap<>();

    public MemoryGameDAO(){}
    public void clear() {
        gameDataHashMap.clear();
    }
//    public GameData createGame(){
//
//    }
//    public GameData getGame(String gameID){
//
//    }
//    public List<GameData> listGames(){
//
//    }
//    public void updateGame(GameData gameData){
//
//    }

}
