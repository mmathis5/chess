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
    private int blackKingRow;
    private int blackKingCol;
    private int whiteKingRow;
    private int whiteKingCol;
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
        if (!isInCheck(teamColor)){
            return false;
        }
        //black case
        if (teamColor == TeamColor.BLACK){
            ArrayList<ChessMove> blackKingMoves = new ArrayList<>();
            blackKingMoves.addAll(validMoves(new ChessPosition(this.blackKingRow, this.blackKingCol)));
            for (int i = 0; i < blackKingMoves.size(); i ++){
                //duplicate the current game board
                ChessBoard unalteredBoard = duplicateBoard();
                //make hypothetical move
                board.removePiece(new ChessPosition(blackKingRow, blackKingCol));
                board.addPiece(blackKingMoves.get(i).getEndPosition(), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
                Boolean resultsInCheck = false;
                if (!isInCheck(TeamColor.BLACK)){
                    //reset board to original state
                    board = unalteredBoard;
                    return false;
                }
                //reset board to original state
                board = unalteredBoard;
            }
        }
        if (teamColor == TeamColor.WHITE){
            ArrayList<ChessMove> whiteKingMoves = new ArrayList<>();
            whiteKingMoves.addAll(validMoves(new ChessPosition(this.whiteKingRow, this.whiteKingCol)));
            for (int i = 0; i < whiteKingMoves.size(); i ++){
                //duplicate this.board
                ChessBoard unalteredBoard = duplicateBoard();
                //make hypothetical moves
                board.removePiece(new ChessPosition(whiteKingRow, whiteKingCol));
                board.addPiece(whiteKingMoves.get(i).getEndPosition(), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
                Boolean resultsInCheck = false;
                if (!isInCheck(TeamColor.WHITE)){
                    //reset board to original state
                    board = unalteredBoard;
                    return false;
                }
                //reset board to original state
                board = unalteredBoard;
            }
        }
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
        //iterate over the whole board and place pieces into my board.
        int row = 1;
        int col = 1;
        while (row < 9){
            while (col < 9){
                if (board.getPiece(new ChessPosition(row, col)) != null){
                    this.board.addPiece(new ChessPosition(row, col), board.getPiece(new ChessPosition(row, col)));
                }
            col += 1;
            }
            col = 1;
            row += 1;
        }
    }

    public ChessBoard duplicateBoard() {
        ChessBoard duplicateBoard = new ChessBoard();
        int row = 1;
        int col = 1;
        while (row < 9){
            while (col < 9){
                if (this.board.getPiece(new ChessPosition(row, col)) != null){
                    duplicateBoard.addPiece(new ChessPosition(row, col), this.board.getPiece(new ChessPosition(row, col)));
                }
                col += 1;
            }
            col = 1;
            row += 1;
        }
        return duplicateBoard;
    }
    public void kingLocations(){
        int row = 1;
        int col = 1;
        int kingRow = 1;
        int kingCol = 1;
        boolean whiteKingFound = false;
        boolean blackKingFound = false;
        //white case
        while (row < 9) {
            while (col < 9) {
                if (this.board.getPiece(new ChessPosition(row, col)) != null){
                    if (this.board.getPiece(new ChessPosition(row, col)).getPieceType() == ChessPiece.PieceType.KING) {
                        if (this.board.getPiece(new ChessPosition(row, col)).getTeamColor() == TeamColor.WHITE){
                            this.whiteKingCol = col;
                            this.whiteKingRow = row;
                            whiteKingFound = true;
                        }
                        if (this.board.getPiece(new ChessPosition(row, col)).getTeamColor() == TeamColor.BLACK){
                            this.blackKingCol = col;
                            this.blackKingRow = row;
                            blackKingFound = true;
                        }
                    }
                }
                col += 1;
            }
            if (blackKingFound && whiteKingFound){
                break;
            }
            col = 1;
            row += 1;
        }
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
