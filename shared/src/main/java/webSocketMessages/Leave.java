package webSocketMessages;

public class Leave  extends UserGameCommand{

    private int gameID;
    public Leave(CommandType commandType, String authToken, int gameID){
        super(commandType, authToken);
        this.gameID = gameID;
    }
}
