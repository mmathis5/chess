package server.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ClearService;
import service.GameService;
import service.UserService;
import webSocketMessages.*;
import webSocketMessages.Error;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;


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
            this.gameService = new GameService(gameDAO, authDAO);
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
            case JOIN_PLAYER -> join_player(message, session);
            case JOIN_OBSERVER ->  join_observer(message, session);
            case MAKE_MOVE -> make_move(message, session);
            case LEAVE -> leave(message, session);
            case RESIGN -> resign(message, session);

        }
    }

    private void join_player(String message, Session session) throws IOException{
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        connections.add(command.getAuthString(), session);
        String localUsername = null;
        try {
            //case where playerColor is provided
            gameService.joinGame(Integer.parseInt(command.getGameid()), command.getPlayerColor().toString(), command.getAuthString());
            localUsername = userService.getUsername(command.getAuthString());
            LoadGame loadGame = new LoadGame(Integer.parseInt(command.getGameid()));
            session.getRemote().sendString(jsonMapper.toJson(loadGame));
            connections.broadcast(command.getAuthString(), jsonMapper.toJson(new Notification(localUsername + " has joined the game as the " + command.getPlayerColor() + "player." )));
        }
        catch (Exception e){
            Error error = new Error("Error Joining Player: " + e.getMessage());
            session.getRemote().sendString(jsonMapper.toJson(error));
        }
    }

    private void join_observer(String message, Session session) throws IOException{
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);
        connections.add(command.getAuthString(), session);
    }
    private void make_move(String message, Session session) throws IOException{
        MakeMove command = new Gson().fromJson(message, MakeMove.class);
        connections.add(command.getAuthString(), session);
    }
    private void leave(String message, Session session) throws IOException{
        Leave command = new Gson().fromJson(message, Leave.class);
        connections.remove(command.getAuthString());
    }

    private void resign(String message, Session session) throws IOException{
        Resign command = new Gson().fromJson(message, Resign.class);
        connections.remove(command.getAuthString());
    }

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}