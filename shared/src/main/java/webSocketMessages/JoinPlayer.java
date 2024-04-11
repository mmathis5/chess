package webSocketMessages;
import chess.ChessGame;
import model.*;

public class JoinPlayer extends UserGameCommand{
    public String getGameid() {
        return gameID;
    }

    private final String gameID;

    public String getPlayerColor() {
        return playerColor;
    }

    //this might be the wrong type
    private final String playerColor;

    public JoinPlayer(String authToken, String gameID, String playerColor){
        super(CommandType.JOIN_PLAYER, authToken);
        this.playerColor = playerColor;
        this.gameID = gameID;

    }

}
