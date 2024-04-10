package server;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class WSServer {

    Set<Session> sessions = new HashSet<Session>();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        sessions.add(session);
        for (Session x : sessions) {
            session.getRemote().sendString("WebSocket response: " + message);
        }
    }
}