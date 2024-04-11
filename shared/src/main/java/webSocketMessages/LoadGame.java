package webSocketMessages;

public class LoadGame extends ServerMessage{
    private final int game;

    public LoadGame(int game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

}
