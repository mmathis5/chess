package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor team;
    private final ChessPiece.PieceType type;


    public ChessPiece(ChessGame.TeamColor team, ChessPiece.PieceType type) {
        this.team = team;
        this.type = type;
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
        return this.team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "TeamColor=" + team +
                ", PieceType=" + type +
                '}';
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
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP || board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            //up right
            while (row < 8 && column < 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, column + 1));
                if (piece != null && piece.getPieceType() != null) {
                    //checks for enemy piece
                    if (piece.getTeamColor() != this.getTeamColor()){
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 1), null));
                row = row+ 1;
                column = column - 1;
            }
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK || board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            //up
            while (0 < row && row < 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, column));
                if (piece != null && piece.getPieceType() != null) {
                    //checks for enemy piece
                    if (piece.getTeamColor() != this.getTeamColor()){
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
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
                    if (piece.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1), null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1), null));
                column = column + 1;
            }
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            //up right
            if (row + 2 < 9 &&  column + 1 < 9) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, column + 1), null));
            }
            //right up
            if (row + 1 < 9 && column + 2 < 9) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 2), null));
            }
            //right down
            if (row - 1 > 0 && column + 2 < 9) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 2), null));
            }
            //down right
            if (row - 2 > 0 && column + 1 < 9) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, column + 1), null));
            }
            //down left
            if (row - 2 > 0 && column - 1 > 0) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, column - 1), null));
            }
            //left down
            if (row- 1 > 0 && column - 2 > 0) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 2), null));
            }
            //left up
            if (row + 1 < 9 && column -2 > 0) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column - 2), null));
            }
            //up left
            if (row + 2 < 9 && column -1 > 0) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, column - 1), null));
            }
            for (int i = 0; i < possibleMoves.size() ; i ++){
                ChessPosition possibleMove = possibleMoves.get(i).getEndPosition();
                ChessPiece piece = board.getPiece(possibleMove);
                if (piece == null){
                    moves.add(new ChessMove(myPosition, possibleMove, null));
                }
                else{
                    if (piece.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, possibleMove, null));
                    }
                }
            }
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KING){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            //up
            if (row < 8) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), null));
            }
            //up right
            if (row < 8 && column < 8) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column + 1), null));
            }
            //right
            if (column < 8) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row, column + 1), null));
            }
            //down right
            if (row > 1 && column < 8) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column + 1), null));
            }
            //down
            if (row > 1) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), null));
            }
            //down left
            if (row > 1 && column > 1) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column - 1), null));
            }
            //left
            if (column > 1) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row, column -1), null));
            }
            //up left
            if (row < 8 && column > 1) {
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column -1), null));
            }

            for (int i = 0; i < possibleMoves.size() ; i ++){
                ChessPosition possibleMove = possibleMoves.get(i).getEndPosition();
                ChessPiece piece = board.getPiece(possibleMove);
                if (piece == null){
                    moves.add(new ChessMove(myPosition, possibleMove, null));
                }
                else{
                    if (piece.getTeamColor() != this.getTeamColor()){
                        moves.add(new ChessMove(myPosition, possibleMove, null));
                    }
                }
            }
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();
            //opening piece White
            if (this.getTeamColor() == ChessGame.TeamColor.WHITE && row == 2){
                ChessPiece singleMove = board.getPiece(new ChessPosition(row +1, column));
                ChessPiece doubleMove = board.getPiece(new ChessPosition(row +2, column));
                if (doubleMove == null && singleMove == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, column), null));
                }
            }
            //opening piece Black
            if (this.getTeamColor() == ChessGame.TeamColor.BLACK && row == 7){
                ChessPiece singleMove = board.getPiece(new ChessPosition(row -1 , column));
                ChessPiece doubleMove = board.getPiece(new ChessPosition(row -2 , column));
                if (doubleMove == null && singleMove == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, column), null));
                }
            }
            //White promotion
            if (this.getTeamColor() == ChessGame.TeamColor.WHITE && row == 7){
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), ChessPiece.PieceType.ROOK));
            }
            //Black promotion
            if (this.getTeamColor() == ChessGame.TeamColor.BLACK && row == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), ChessPiece.PieceType.ROOK));
            }
            //black standard moves
            if (this.getTeamColor() == ChessGame.TeamColor.BLACK){
                //check in front of pawn
                ChessPiece piece = board.getPiece(new ChessPosition(row -1, column));
                if (piece == null && row -1 != 1){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, column), null));
                }
                //check for possible kills
                ChessPiece downRightKill = null;
                ChessPiece downLeftKill = null;
                //downRightKill invalid for black col 1
                if (column != 8) {
                    downRightKill = board.getPiece(new ChessPosition(row - 1, column + 1));
                }
                //downLeftKill invalid for black col 1
                if (column != 1){
                    downLeftKill = board.getPiece(new ChessPosition(row -1, column - 1));
                }
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
            if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                //check in front of pawn
                ChessPiece piece = board.getPiece(new ChessPosition(row +1, column));
                if (piece == null && row + 1 != 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, column), null));
                }
                //check for possible kills
                ChessPiece upRightKill = null;
                ChessPiece upLeftKill = null;
                //upRightKill invalid for col 8
                if (column != 8){
                    upRightKill = board.getPiece(new ChessPosition(row+1, column+1));
                }
                //upLeftKill invalid for col 1
                if (column != 1){
                    upLeftKill = board.getPiece(new ChessPosition(row+1, column-1));
                }
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
        }
        return moves;
    }
}
