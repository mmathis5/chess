package webSocketMessages;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{

    public int getGameID() {
        return gameID;
    }

    private final int gameID;

    public ChessMove getChessMove() {
        return move;
    }

    private final ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move){
        super(CommandType.MAKE_MOVE, authToken);
        this.gameID = gameID;
        this.move = move;
    }
}
