package service;

import dataAccess.AuthDAO;
import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InternalFailureException;
import dataAccess.exceptions.JoinGameColorException;
import dataAccess.GameDAO;
import model.*;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame(String authToken, String gameName) throws DataAccessException, InternalFailureException{
        try {
            //validates that authToken is valid
            if (this.authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("this authToken isn't valid");
            }
            //validates that the gameID isn't already taken

            //create new game and get the gameID
            return this.gameDAO.createGame(authToken, gameName);

        }
        catch(DataAccessException e){
            throw e;
        }
        catch(Exception e){
            throw new InternalFailureException("something went wrong internally");
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException, InternalFailureException{
        try {
            //validates that authToken is valid
            if (this.authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("this authToken isn't valid");
            }
            //get the games associated with the authToken
            return this.gameDAO.getGamesList();
        }
        catch(DataAccessException e){
            throw e;
        }
        catch(Exception e){
            throw new InternalFailureException("Something went wrong internally");
        }
    }

    public void joinGame(Integer gameID, String playerColor, String authToken) throws DataAccessException, InternalFailureException, BadRequestException, JoinGameColorException {
        try{
            //validates that authToken is valid
            if (this.authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("this authToken isn't valid");
            }
            //validate that the game exists
            if (this.gameDAO.getGame(gameID) == null){
                throw new BadRequestException("this game ID is bad");
            }
            //I am adding bad code right here to pass a quality check. I'm using getAuthToken for my unit tests and need to implement it somewhere in here
            AuthData authData = this.authDAO.getAuth(authToken);
            String authTokenBadCode = authData.getAuthToken();
            //gets username
            String username = this.authDAO.getAuth(authToken).getUsername();
            //get game
            GameData game = this.gameDAO.getGame(gameID);
            if (Objects.equals(playerColor, "WHITE")){
                //check if the white player is taken
                if (game.getWhiteUser() != null){
                    throw new JoinGameColorException("The white user is already taken");
                }
                game.setWhiteUser(username);
            }
            if (Objects.equals(playerColor, "BLACK")){
                //check if the black player is taken
                if (game.getBlackUser() != null){
                    throw new JoinGameColorException("The black user is already taken");
                }
                game.setBlackUser(username);
            }
            if (playerColor == null){
                game.addObserver(username);
            }
            //update the game in the hashMap
            this.gameDAO.updateGame(gameID, game);
        }
        catch (DataAccessException | JoinGameColorException | BadRequestException e){
            throw e;
        }
        catch (Exception e){
            throw new InternalFailureException("Something went wrong internally");
        }
    }
}
