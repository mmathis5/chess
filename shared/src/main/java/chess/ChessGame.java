package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //returns the array of possible moves from ChessPiece.java
        return this.board.getPiece(startPosition).pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.removePiece(move.getStartPosition());
        //promotion case
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
            //The getPromotionPieceMethod return the ChessPiece.PieceType attribute, not the whole piece type.
            //I just made a new piece to fix this, but I don't think that's a very
            //elegant solution. Can I change the expected return type of the function?
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else{
            board.addPiece(move.getEndPosition(), piece);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        int row = 1;
        int col = 1;
        int kingRow = 1;
        int kingCol = 1;
        boolean kingFound = false;
        //white case
        if (teamColor == TeamColor.WHITE){
            //find location of King
            while (row < 9) {
                while (col < 9) {
                    if (this.board.getPiece(new ChessPosition(row, col)) != null){
                        if (this.board.getPiece(new ChessPosition(row, col)).getPieceType() == ChessPiece.PieceType.KING) {
                            kingRow = row;
                            kingCol = col;
                            kingFound = true;
                            break;
                        }
                    }
                    col += 1;
                }
                if (kingFound){
                    break;
                }
                col = 1;
                row += 1;
            }
        }
        if (teamColor == TeamColor.BLACK) {
            //find location of King
            row = 8;
            col = 1;
            while (row > 0) {
                while (col < 9) {
                    if (this.board.getPiece(new ChessPosition(row, col)) != null) {
                        if (this.board.getPiece(new ChessPosition(row, col)).getPieceType() == ChessPiece.PieceType.KING) {
                            kingRow = row;
                            kingCol = col;
                            break;
                        }
                    }
                    col += 1;
                }
                col = 1;
                row -= 1;
            }
        }
        //reset row and col for final check
        row = 1;
        col = 1;
        while (row < 9){
            while (col < 9){
                //check if the King's position is in the possible moves list
                if (this.board.getPiece(new ChessPosition(row, col)) != null && this.board.getPiece(new ChessPosition(row, col)).getTeamColor() != teamColor){
                    ChessPiece currPiece = this.board.getPiece(new ChessPosition(row, col));
                    ArrayList<ChessMove> currPieceMoves = new ArrayList<>();
                    currPieceMoves.addAll(validMoves(new ChessPosition(row, col)));
                    for (int i = 0; i < currPieceMoves.size(); i++){
                        if (currPieceMoves.get(i).getEndPosition().getRow() == kingRow && currPieceMoves.get(i).getEndPosition().getColumn() == kingCol){
                            return true;
                        }
                    }
                }
                col += 1;
            }
            col = 1;
            row += 1;
        }
        //otherwise
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //find the location of the king
        //get every possible move
        //loop through them and return false if isInCheck return false
        return true;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

}
