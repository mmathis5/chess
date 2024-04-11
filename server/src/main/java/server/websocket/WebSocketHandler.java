package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.UserGameCommand;
import webSocketMessages.ServerMessage;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> join_player(userGameCommand.getAuthString(), session);
            case JOIN_OBSERVER ->  join_observer(userGameCommand.getAuthString(), session);
            case MAKE_MOVE -> make_move(userGameCommand.getAuthString(), session);
            case LEAVE -> leave(userGameCommand.getAuthString(), session);
            case RESIGN -> resign(userGameCommand.getAuthString(), session);

        }
    }

    private void join_player(String authToken, Session session) throws IOException{

    }

    private void join_observer(String authToken, Session session) throws IOException{

    }
    private void make_move(String authToken, Session session) throws IOException{

    }
    private void leave(String authToken, Session session) throws IOException{

    }

    private void resign(String authToken, Session session) throws IOException{

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