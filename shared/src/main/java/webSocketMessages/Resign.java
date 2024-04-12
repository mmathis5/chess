package webSocketMessages;

public class Resign extends UserGameCommand{
    public int getGameID() {
        return gameID;
    }

    private int gameID;
    public Resign(String authToken, int gameID){
        super(CommandType.RESIGN, authToken);
        this.gameID = gameID;
    }
}
