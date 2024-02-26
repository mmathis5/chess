package dataAccess;

import com.sun.management.GarbageCollectionNotificationInfo;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface GameDAO {
    public void clear();

    public Integer createGame(String authToken, String gameName)        ;
    public ArrayList<GameData> getGamesList();
    public GameData getGame(Integer gameID);
//    public List<GameData> listGames();
//    public void updateGame(GameData gameData);

}
