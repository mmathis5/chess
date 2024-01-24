package chess.pieces;

import chess.ChessMove;
import chess.ChessPosition;
import chess.*;
import java.util.Collection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Rook {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor myColor;
    private Collection<ChessMove> possibleMoves;

    public Rook(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.myColor = myColor;
    }

    public Collection<ChessMove> pieceMovesRook() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        //up
        while (0 < row && row < 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(row + 1, column));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), null));
            row = row + 1;
        }
        //reset variables
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //left
        while (column < 8 && column > 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, column - 1) );
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, column - 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row, column - 1), null));
            column = column - 1;
        }
        //reset position again
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //down
        while ((1 < row)) {
            ChessPiece piece = board.getPiece(new ChessPosition(row -1, column));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row -1, column), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), null));
            row = row - 1;
        }
        row = myPosition.getRow();
        column = myPosition.getColumn();
        //right
        while (column < 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, column + 1));
            if (piece != null && piece.getPieceType() != null) {
                //checks for enemy piece
                if (piece.getTeamColor() != this.myColor) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1), null));
                }
                break;
            }
            moves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1), null));
            column = column + 1;
        }
        return moves;
    }
}