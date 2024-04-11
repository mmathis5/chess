package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.*;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
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