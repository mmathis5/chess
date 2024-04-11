package webSocketMessages;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{

    private final int gameID;
    private final ChessMove chessMove;

    public MakeMove(CommandType commandType, String authToken, int gameID, ChessMove chessMove){
        super(commandType, authToken);
        this.gameID = gameID;
        this.chessMove = chessMove;
    }
}
