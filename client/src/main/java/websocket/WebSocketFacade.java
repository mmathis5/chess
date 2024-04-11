package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.nio.sctp.NotificationHandler;
import webSocketMessages.*;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    websocket.NotificationHandler notificationHandler;
    private Gson jsonMapper = new Gson();


    public WebSocketFacade(String url, websocket.NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http:", "ws://");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println("Message Received: " + message);
                    try {
                        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                        notificationHandler.notify(message, notification.getServerMessageType());
                    }
                    catch (Exception e){
                        System.out.println("unable to read message: " + e.getMessage());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, String gameID, String playerColor) throws Exception {
        try{
            JoinPlayer joinPlayer = new JoinPlayer(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(jsonMapper.toJson(joinPlayer));
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            throw e;
        }

    }
    public void joinObserver(String authToken, String gameID) throws IOException {
        try{
            JoinObserver joinObserver = new JoinObserver(authToken, Integer.parseInt(gameID));
            this.session.getBasicRemote().sendText(jsonMapper.toJson(joinObserver));
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }
    public void leaveGame(String visitorName) throws Exception{

    }
    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception{
        try{
            MakeMove makeMove = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(jsonMapper.toJson(makeMove));
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            throw e;
        }
    }


}