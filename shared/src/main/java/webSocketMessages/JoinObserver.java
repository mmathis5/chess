package webSocketMessages;

import chess.ChessGame;
import model.UserData;

public class JoinObserver extends UserGameCommand {
    private final Integer gameid;

    public JoinObserver(UserGameCommand.CommandType commandType, String authToken, int gameID){
        super(commandType, authToken);
        this.gameid = gameID;

    }
}
