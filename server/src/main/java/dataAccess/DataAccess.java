package dataAccess;

import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;
public interface DataAccess {
    //clear endpoint
    void clear();

    //register endpoint
    UserData register(String username, String password, String email) throws DataAccessException;

    //login endpoint
    UserData login(String username, String password, String email) throws DataAccessException;

    //logout endpoint
    void logout();

    //List Games Endpoint
    //??
    Collection<GameData> listGames();

    //Create Game Endpoint
    GameData createGame(String gameName) throws DataAccessException;

    //Join Game Endpoint
    GameData joinGame(String playerColor, String gameID);

}
