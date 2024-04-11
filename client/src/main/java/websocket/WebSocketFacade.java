package websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;


import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    websocket.NotificationHandler notificationHandler;


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
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify();
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

    public void joinGame(String visitorName) throws Exception {

    }
    public void leaveGame(String visitorName) throws Exception{

    }
    public void makeMove(String visitorName) throws Exception{

    }


}