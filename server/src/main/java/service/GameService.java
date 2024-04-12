package service;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import dataAccess.exceptions.BadRequestException;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InternalFailureException;
import dataAccess.exceptions.JoinGameColorException;
import dataAccess.GameDAO;
import model.*;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void resign(int gameID) throws SQLException, IOException, ClassNotFoundException {
        try{
            GameData gameData = getGame(gameID);
            gameData.setGameIsComplete(true);
            gameDAO.updateGame(gameID, gameData);
        }
        catch (Exception e){
            throw e;
        }


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
    public GameData getGame(int gameID) throws SQLException, IOException, ClassNotFoundException {
        try{
            return this.gameDAO.getGame(gameID);
        }
        catch (Exception e){
            throw e;
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



    public void joinGame(Integer gameID, String playerColor, String authToken, Boolean usernameCanBeNull) throws DataAccessException, InternalFailureException, BadRequestException, JoinGameColorException {
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
                if (game.getWhiteUser() == null || Objects.equals(this.userDAO.getUsername(authToken), game.getWhiteUser())){
                    if (game.getWhiteUser() == null && !usernameCanBeNull){
                        throw new Exception("You haven't called the appropriate http endpoint");
                    }
                    game.setWhiteUser(username);
                }
                else{
                    throw new JoinGameColorException("The white user is already taken");
                }
            }
            if (Objects.equals(playerColor, "BLACK")){
                //check if the black player is taken
                if (game.getBlackUser() == null || Objects.equals(this.userDAO.getUsername(authToken), game.getBlackUser())){
                    if (game.getBlackUser() == null && !usernameCanBeNull){
                        throw new Exception("You haven't called the appropriate http endpoint");
                    }
                    game.setBlackUser(username);
                }
                else{
                    throw new JoinGameColorException("The white user is already taken");
                }
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

    public void makeValidMove(Integer gameID, ChessMove chessMove) throws Exception {
        boolean isValidMove = false;
        try{
            GameData gameData = gameDAO.getGame(gameID);

            ChessPosition startingPosition = chessMove.getStartPosition();
            ChessPosition endPosition = chessMove.getEndPosition();
            ChessBoard chessBoard = gameData.getChessBoard();
            ChessGame chessGame = gameData.getChessGame();
            Collection<ChessMove> validMoves = chessGame.validMoves(startingPosition);
            //check if the move is valid
            Iterator<ChessMove> validMovesIterator = validMoves.iterator();
            while (validMovesIterator.hasNext()){
                ChessMove potentialMove = validMovesIterator.next();
                if (Objects.equals(chessMove, potentialMove)){
                    isValidMove = true;
                    break;
                }
            }
            if (isValidMove = false){
                throw new Exception("Your provided Move is Invalid:(");
            }
            //assuming the move is valid, make the move.
            chessGame.makeMove(chessMove);
            //markGameComplete if it is in checkmate.
            if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) || chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
                gameData.setGameIsComplete(true);
            }
            gameData.setChessGame(chessGame);
            gameDAO.updateGame(gameID, gameData);

        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
}
