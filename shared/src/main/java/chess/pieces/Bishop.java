package chess.pieces;

import chess.ChessMove;
import chess.ChessPosition;
import chess.*;
import java.util.Collection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bishop {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor myColor;
    private Collection<ChessMove> possibleMoves;

    public Bishop(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor){
        this.board = board;
        this.myPosition = myPosition;
        this.myColor = myColor;
    }

    // this accounts for other pieces that would block the move.
    public Collection<ChessMove> pieceMovesBishop() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        //Bishop moves diagonally in any direction
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        //up right
        while (row < 8 && column < 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(row + 1, column + 1));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), null));
            row = row + 1;
            column = column + 1;
        }
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //down right
        while (row > 1 && column < 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(row -1, column + 1) );
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), null));
            row = row - 1;
            column = column + 1;
        }
        //reset position again
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //down left
        while ((1 < row && 1 < column)) {
            ChessPiece piece = board.getPiece(new ChessPosition(row - 1, column - 1));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), null));
            row = row - 1;
            column = column - 1;
        }
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //up left
        while (8 > row && column > 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(row + 1, column - 1));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), null));
            row = row+ 1;
            column = column - 1;
        }
           return moves;
    }
}
