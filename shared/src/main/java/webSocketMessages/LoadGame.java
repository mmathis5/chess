package webSocketMessages;

public class LoadGame extends ServerMessage{
    private final String game;

    public LoadGame(ServerMessageType type, String game){
        super(type);
        this.game = game;
    }
}
