package websocket;
public interface NotificationHandler {
    void notify(ServerMessage.ServerMessageType serverMessageType);
}
