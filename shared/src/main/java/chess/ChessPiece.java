package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor TeamColor;
    private final ChessPiece.PieceType PieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.TeamColor = pieceColor;
        this.PieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.TeamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.PieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        //Bishop moves diagonally in any direction
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            //up right
            while (0 < row && row < 8 && column < 8 && column > 0){
                moves.add(new ChessMove( new ChessPosition(row, column), new ChessPosition(row + 1,column + 1), PieceType.BISHOP));
                row = row + 1;
                column = column + 1;
            }
            //reset position again
            int row2 = myPosition.getRow();
            int column2 = myPosition.getColumn();
            //down right
            while (0 < row2 && row2 < 8 && column2 < 8 && column2 > 0){
                moves.add(new ChessMove( new ChessPosition(row2, column2), new ChessPosition(row2 - 1,column2 + 1), PieceType.BISHOP));
                row2 = row2 - 1;
                column2 = column2 + 1;
            }

            //reset position again
            int row3 = myPosition.getRow();
            int column3 = myPosition.getColumn();
            //down left
            while ((0 < row3 && row3 < 8 && 0 < column3 && column3 < 8)){
                moves.add(new ChessMove( new ChessPosition(row3, column3), new ChessPosition(row3 - 1,column3 - 1), PieceType.BISHOP));
                row3 = row3 - 1;
                column3 = column3 - 1;
            }
            //reset local variables. I had to rename them but this is messy and I hate it:/
            int row1 = myPosition.getRow();
            int column1 = myPosition.getColumn();
            //up left
            while (0 < row1 && row1 < 8 && column1 < 8 && column > 0){
                moves.add(new ChessMove( new ChessPosition(row1, column1), new ChessPosition(row1 + 1,column1 - 1), PieceType.BISHOP));
                row1 = row1 + 1;
                column1 = column1 - 1;
            }
        }
        return moves;

    }
}
