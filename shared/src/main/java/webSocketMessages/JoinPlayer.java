package webSocketMessages;
import chess.ChessGame;
import model.*;

public class JoinPlayer extends UserGameCommand{
    private final Integer gameid;
    //this might be the wrong type
    private final ChessGame.TeamColor playerColor;

    public JoinPlayer(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessGame.TeamColor playerColor){
        super(commandType, authToken);
        this.playerColor = playerColor;
        this.gameid = gameID;

    }
}
