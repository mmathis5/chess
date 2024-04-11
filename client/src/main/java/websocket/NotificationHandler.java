package websocket;
import webSocketMessages.ServerMessage;
public interface NotificationHandler {
    void notify(String message, ServerMessage.ServerMessageType type);
}
