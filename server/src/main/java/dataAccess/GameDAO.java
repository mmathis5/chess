package dataAccess;

import com.sun.management.GarbageCollectionNotificationInfo;
import model.GameData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface GameDAO {
    public void clear();

    public Integer createGame(String authToken, String gameName);
    public ArrayList<GameData> getGamesList();
    public GameData getGame(Integer gameID) throws SQLException, IOException, ClassNotFoundException;

    public void updateGame(Integer gameID, GameData gameData);

}
