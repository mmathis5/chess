package chess.pieces;

import chess.ChessMove;
import chess.ChessPosition;
import chess.*;
import java.util.Collection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Knight {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor myColor;
    private Collection<ChessMove> possibleMoves;

    public Knight(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.myColor = myColor;
    }

    public Collection<ChessMove> pieceMovesKnight() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //up right
        if (row + 2 < 9 &&  column + 1 < 9) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 2, column + 1), null));
        }
        //right up
        if (row + 1 < 9 && column + 2 < 9) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 1, column + 2), null));
        }
        //right down
        if (row - 1 > 0 && column + 2 < 9) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 1, column + 2), null));
        }
        //down right
        if (row - 2 > 0 && column + 1 < 9) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 2, column + 1), null));
        }
        //down left
        if (row - 2 > 0 && column - 1 > 0) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 2, column - 1), null));
        }
        //left down
        if (row- 1 > 0 && column - 2 > 0) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 1, column - 2), null));
        }
        //left up
        if (row + 1 < 9 && column -2 > 0) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 1, column - 2), null));
        }
        //up left
        if (row + 2 < 9 && column -1 > 0) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 2, column - 1), null));
        }
        for (int i = 0; i < possibleMoves.size() ; i ++){
            ChessPosition possibleMove = possibleMoves.get(i).getEndPosition();
            ChessPiece piece = board.getPiece(possibleMove);
            if (piece == null){
                moves.add(new ChessMove(myPosition, possibleMove, null));
            }
            else{
                if (piece.getTeamColor() != this.myColor){
                    moves.add(new ChessMove(myPosition, possibleMove, null));
                }
            }
        }
        return moves;
    }
}