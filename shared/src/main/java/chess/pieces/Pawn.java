package chess.pieces;

import chess.ChessMove;
import chess.ChessPosition;
import chess.*;
import java.util.Collection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Pawn {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor myColor;
    private Collection<ChessMove> possibleMoves;

    public Pawn(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        this.board = board;
        this.myPosition = myPosition;
        this.myColor = myColor;
    }

    public Collection<ChessMove> pieceMovesPawn() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        //opening piece White
        if (this.myColor == ChessGame.TeamColor.WHITE && row == 2){
            ChessPiece singleMove = board.getPiece(new ChessPosition(row +1, column));
            ChessPiece doubleMove = board.getPiece(new ChessPosition(row +2, column));
            if (doubleMove == null && singleMove == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, column), null));
            }
        }
        //opening piece Black
        if (this.myColor == ChessGame.TeamColor.BLACK && row == 7){
            ChessPiece singleMove = board.getPiece(new ChessPosition(row -1 , column));
            ChessPiece doubleMove = board.getPiece(new ChessPosition(row -2 , column));
            if (doubleMove == null && singleMove == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, column), null));
            }
        }
        //White promotion
        if (this.myColor == ChessGame.TeamColor.WHITE && row == 7){
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.ROOK));
        }
        //Black promotion
        if (this.myColor == ChessGame.TeamColor.BLACK && row == 2){
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.ROOK));
        }
        //black standard moves
        if (this.myColor == ChessGame.TeamColor.BLACK){
            //check in front of pawn
            ChessPiece piece = board.getPiece(new ChessPosition(row -1, column));
            if (piece == null && row -1 != 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), null));
            }
            //check for possible kills
            ChessPiece downRightKill = board.getPiece(new ChessPosition(row -1, column + 1));
            ChessPiece downLeftKill = board.getPiece(new ChessPosition(row -1, column - 1));
            if (downRightKill != null && downRightKill.getTeamColor() == ChessGame.TeamColor.WHITE){
                //check if the right kill will result in a promotion
                if (row -1 == 1){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(row -1, column + 1), null));
                }
            }
            if (downLeftKill != null && downLeftKill.getTeamColor() == ChessGame.TeamColor.WHITE){
                if (row - 1 == 1){
                    //check if the left kill will result in a promotion
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), null));
                }
            }
        }
        //white standard moves
        if (this.myColor == ChessGame.TeamColor.WHITE) {
            //check in front of pawn
            ChessPiece piece = board.getPiece(new ChessPosition(row +1, column));
                if (piece == null && row + 1 != 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), null));
                }
            //check for possible kills
            ChessPiece upRightKill = board.getPiece(new ChessPosition(row+1, column+1));
            ChessPiece upLeftKill = board.getPiece(new ChessPosition(row+1, column-1));
            if (upRightKill != null && upRightKill.getTeamColor() == ChessGame.TeamColor.BLACK){
                //check if the up right kill will result in a promotion
                if (row + 1 == 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(row+1, column + 1), null));
                }
            }
            if (upLeftKill != null && upLeftKill.getTeamColor() == ChessGame.TeamColor.BLACK){
                //check if up left kill will result in a promotion
                if (row + 1 == 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), ChessPiece.PieceType.ROOK));
                }
                else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), null));
                }
            }
        }
        return moves;
    }
}