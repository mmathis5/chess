package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.*;
import webSocketMessages.Error;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private static GameService gameService;
    private static UserService userService;

    private final ConnectionManager connections = new ConnectionManager();
    private Gson jsonMapper = new Gson();

    public WebSocketHandler(){
        try {
            DatabaseManager.init();
            GameDAO gameDAO = new SQLGameDAO();
            UserDAO userDAO = new SQLUserDAO();
            AuthDAO authDAO = new SQLAuthDAO();
            this.userService = new UserService(userDAO, authDAO);
            this.gameService = new GameService(gameDAO, authDAO, userDAO);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message received: " + message);
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER ->  joinObserver(message, session);
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> leave(message, session);
            case RESIGN -> resign(message, session);

        }
    }

    private void joinPlayer(String message, Session session) throws IOException{
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        connections.add(command.getAuthString(), session);
        String localUsername = null;
        try {
            gameService.joinGame(Integer.parseInt(command.getGameid()), command.getPlayerColor().toString(), command.getAuthString(), false);
            localUsername = userService.getUsername(command.getAuthString());
            LoadGame loadGame = new LoadGame(Integer.parseInt(command.getGameid()));
            session.getRemote().sendString(jsonMapper.toJson(loadGame));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has joined the game as the " + command.getPlayerColor() + " player." )));
        }
        catch (Exception e){
            Error error = new Error("Error Joining Player: " + e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }

    private void joinObserver(String message, Session session) throws IOException{
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);
        connections.add(command.getAuthString(), session);
        String localUsername = null;
        try{
            gameService.joinGame(command.getGameID(), null, command.getAuthString(), true);
            localUsername = userService.getUsername(command.getAuthString());
            LoadGame loadGame = new LoadGame(command.getGameID());
            session.getRemote().sendString(jsonMapper.toJson(loadGame));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has joined the game as an observer")));

        }
        catch (Exception e){
            Error error = new Error("Error Joining Player As Observer: " + e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }
    private void makeMove(String message, Session session) throws IOException{
        MakeMove command = new Gson().fromJson(message, MakeMove.class);
        connections.add(command.getAuthString(), session);
        String localUsername = null;
        try{
            //get the chess move
            ChessMove desiredMove = command.getChessMove();
            Integer gameID = command.getGameID();
            GameData gameData = gameService.getGame(gameID);
            //check if the game is complete
            if (gameData.getGameIsComplete()){
                throw new Exception("This game is complete. Please leave it at your leisure");
            }
            //check if this is an observer
            localUsername = userService.getUsername(command.getAuthString());
            if (!Objects.equals(gameData.getWhiteUser(), localUsername) && !Objects.equals(gameData.getBlackUser(), localUsername)){
                throw new Exception("An observer cannot make a move");
            }
            gameService.makeValidMove(gameID, desiredMove);

            LoadGame loadGame = new LoadGame(command.getGameID());
            session.getRemote().sendString(jsonMapper.toJson(loadGame));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has just made a move")));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(loadGame));
        }
        catch (Exception e){
            Error error = new Error("Error making move: " + e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }
    private void leave(String message, Session session) throws IOException{
        Leave command = new Gson().fromJson(message, Leave.class);
        connections.remove(command.getAuthString());
        try {
            String localUsername = userService.getUsername(command.getAuthString());
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has left the game")));
        }
        catch(Exception e){
            Error error = new Error("Error leaving: " + e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }

    private void resign(String message, Session session) throws IOException{
        Resign command = new Gson().fromJson(message, Resign.class);
        connections.remove(command.getAuthString());
        try{
            String localUsername = userService.getUsername(command.getAuthString());
            Integer gameID = command.getGameID();
            GameData gameData = gameService.getGame(gameID);
            localUsername = userService.getUsername(command.getAuthString());
            //check if the game is complete
            if (!Objects.equals(gameData.getWhiteUser(), localUsername) && !Objects.equals(gameData.getBlackUser(), localUsername)){
                throw new Exception("An observer cannot resign");
            }
            if (gameData.getGameIsComplete()){
                throw new Exception("This game is complete. As such, you cannot resign");
            }

            gameService.resign(command.getGameID());
            session.getRemote().sendString(jsonMapper.toJson(new Notification(localUsername + " has resigned from the game")));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has resigned from the game")));
        }
        catch(Exception e){
            Error error = new Error("Error Resigning: "+ e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }

}