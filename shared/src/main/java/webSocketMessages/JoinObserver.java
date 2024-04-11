package webSocketMessages;

import chess.ChessGame;
import model.UserData;

public class JoinObserver extends UserGameCommand {
    private final Integer gameID;
    public Integer getGameID() {
        return gameID;
    }

    public JoinObserver(String authToken, Integer gameID){
        super(CommandType.JOIN_OBSERVER, authToken);
        this.gameID = gameID;

    }
}
