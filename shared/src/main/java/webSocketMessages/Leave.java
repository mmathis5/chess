package webSocketMessages;

public class Leave  extends UserGameCommand{

    private int gameID;
    public Leave(String authToken, int gameID){
        super(CommandType.LEAVE, authToken);
        this.gameID = gameID;
    }
}
