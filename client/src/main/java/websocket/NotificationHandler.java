package websocket;
import webSocketMessages.ServerMessage;
public interface NotificationHandler {
    void notify(ServerMessage.ServerMessageType serverMessageType);
}
