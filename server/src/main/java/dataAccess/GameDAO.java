package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public interface GameDAO {
    public GameData createGame();
    public GameData getGame(String gameID);
    public List<GameData> listGames();
    public void updateGame(GameData gameData);
    public void clear();
}
