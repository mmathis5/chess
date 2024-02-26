package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.InternalFailureException;
import dataAccess.Exceptions.JoinGameColorException;
import dataAccess.GameDAO;
import model.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
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
        }
        catch (DataAccessException e){
            throw e;
        }
        catch (JoinGameColorException e){
            throw e;
        }
        catch (Exception e){
            throw new InternalFailureException("Something went wrong internally");
        }
    }
}
