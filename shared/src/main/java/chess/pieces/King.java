package chess.pieces;

import chess.ChessMove;
import chess.ChessPosition;
import chess.*;
import java.util.Collection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class King {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor myColor;
    private Collection<ChessMove> possibleMoves;

    public King(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.myColor = myColor;
    }

    public Collection<ChessMove> pieceMovesKing() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        return moves;
    }
}