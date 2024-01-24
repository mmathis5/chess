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
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        //up
        if (row < 8) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 1, column), null));
        }
        //up right
        if (row < 8 && column < 8) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 1, column + 1), null));
        }
        //right
        if (column < 8) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row, column + 1), null));
        }
        //down right
        if (row > 1 && column < 8) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 1, column + 1), null));
        }
        //down
        if (row > 1) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 1, column), null));
        }
        //down left
        if (row > 1 && column > 1) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row - 1, column - 1), null));
        }
        //left
        if (column > 1) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row, column -1), null));
        }
        //up left
        if (row < 8 && column > 1) {
            possibleMoves.add(new ChessMove(this.myPosition, new ChessPosition(row + 1, column -1), null));
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