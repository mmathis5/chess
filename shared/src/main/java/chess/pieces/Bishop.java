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
        while (1 < row && row < 8 && column < 8 && column > 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), null));
            row = row + 1;
            column = column + 1;
        }
        //reset position again
        int row2 = myPosition.getRow();
        int column2 = myPosition.getColumn();
        //down right
        while (1 < row2 && row2 < 8 && column2 < 8 && column2 > 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row2 - 1, column2 + 1), null));
            row2 = row2 - 1;
            column2 = column2 + 1;
        }

        //reset position again
        int row3 = myPosition.getRow();
        int column3 = myPosition.getColumn();
        //down left
        while ((1 < row3 && row3 < 8 && 1 < column3 && column3 < 8)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row3 - 1, column3 - 1), null));
            row3 = row3 - 1;
            column3 = column3 - 1;
        }
        //reset local variables. I had to rename them but this is messy and I hate it:/
        //I could probably set it up in a for loop but I really don't want to:/
        int row1 = myPosition.getRow();
        int column1 = myPosition.getColumn();
        //up left
        while (1 < row1 && row1 < 8 && column1 < 8 && column > 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(row1 + 1, column1 - 1), null));
            row1 = row1 + 1;
            column1 = column1 - 1;
        }
           return moves;
    }
}
