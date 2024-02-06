package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    private InvalidMoveException invalidMoveException;
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
        //figure out piece type and color
        ChessPiece piece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = board.getPiece(startPosition).getTeamColor();
        //returns the array of possible moves from ChessPiece.java
        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        potentialMoves.addAll(this.board.getPiece(startPosition).pieceMoves(board, startPosition));
        for (int i = 0; i < potentialMoves.size(); i++) {
            ChessMove move = potentialMoves.get(i);
            ChessPosition endPosition = move.getEndPosition();
            //duplicate the current game board
            ChessBoard unalteredBoard = duplicateBoard();
            //make hypothetical move
            board.removePiece(startPosition);
            board.addPiece(endPosition, piece);
            if (!isInCheck(teamColor)){
                moves.add(potentialMoves.get(i));
            }
            board = unalteredBoard;
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //duplicate board
        ChessBoard initialBoard = duplicateBoard();
        ChessPiece piece = board.getPiece(move.getStartPosition());
        //confirm that it is their turn
        if (piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }
        //check if move is invalid because there is no piece there
        if (piece == null){
            throw new InvalidMoveException("there is no piece at the starting location");
        }
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(this.board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()));
        ChessGame.TeamColor team = board.getPiece(move.getStartPosition()).getTeamColor();
        board.removePiece(move.getStartPosition());
        if (!moves.contains(move)){
            board = initialBoard;
            throw new InvalidMoveException();
        }
       if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
            //The getPromotionPieceMethod return the ChessPiece.PieceType attribute, not the whole piece type.
            //I just made a new piece to fix this, but I don't think that's a very
            //elegant solution. Can I change the expected return type of the function?
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        //take the move
        board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
        //check if move results in check
        if (isInCheck(team)){
            board = initialBoard;
            throw new InvalidMoveException();
        }

        if (piece.getTeamColor() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        if (piece.getTeamColor() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
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
        if (teamColor == TeamColor.WHITE){
            ChessPosition king = getWhiteKingLocation();
            kingRow = king.getRow();
            kingCol = king.getColumn();
        }
        if (teamColor == TeamColor.BLACK){
            ChessPosition king = getBlackKingLocation();
            kingRow = king.getRow();
            kingCol = king.getColumn();
        }
        //reset row and col for final check
        row = 1;
        col = 1;
        while (row < 9){
            while (col < 9){
                //check if the King's position is in the possible moves list
                if (this.board.getPiece(new ChessPosition(row, col)) != null && this.board.getPiece(new ChessPosition(row, col)).getTeamColor() != teamColor){
                    ArrayList<ChessMove> currPieceMoves = new ArrayList<>();
                    currPieceMoves.addAll(board.getPiece(new ChessPosition(row, col)).pieceMoves(board, new ChessPosition(row, col)));
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
        int kingRow = 0;
        int kingCol = 0;
        //black case
        if (teamColor == TeamColor.BLACK){
            ChessPosition kingPosition = getBlackKingLocation();
            kingRow = kingPosition.getRow();
            kingCol = kingPosition.getColumn();
            ArrayList<ChessMove> blackKingMoves = new ArrayList<>();
            blackKingMoves.addAll(board.getPiece(new ChessPosition(kingRow, kingCol)).pieceMoves(board, new ChessPosition(kingRow, kingCol)));
            for (int i = 0; i < blackKingMoves.size(); i ++){
                //duplicate the current game board
                ChessBoard unalteredBoard = duplicateBoard();
                //make hypothetical move
                board.removePiece(kingPosition);
                board.addPiece(blackKingMoves.get(i).getEndPosition(), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING));
                if (!isInCheck(TeamColor.BLACK)){
                    //reset board to original state
                    board = unalteredBoard;
                    return false;
                }
                //reset board to original state
                board = unalteredBoard;
            }
        }
        //white case
        if (teamColor == TeamColor.WHITE){
            ChessPosition kingPosition = getWhiteKingLocation();
            kingRow = kingPosition.getRow();
            kingCol = kingPosition.getColumn();
            ArrayList<ChessMove> whiteKingMoves = new ArrayList<>();
            whiteKingMoves.addAll(board.getPiece(new ChessPosition(kingRow, kingCol)).pieceMoves(board, new ChessPosition(kingRow, kingCol)));
            for (int i = 0; i < whiteKingMoves.size(); i ++){
                //duplicate this.board
                ChessBoard unalteredBoard = duplicateBoard();
                //make hypothetical moves
                board.removePiece(kingPosition);
                board.addPiece(whiteKingMoves.get(i).getEndPosition(), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING));
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
        int row = 1;
        int col = 1;
        while (row < 9){
            while (col < 9){
                ArrayList<ChessMove> moves = new ArrayList<>();
                if (board.getPiece(new ChessPosition(row, col) )!= null){
                    if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == teamColor){
                        moves.addAll(validMoves(new ChessPosition(row, col)));
                        if (!moves.isEmpty()){
                            return false;
                        }
                    }
                }
                col += 1;
            }
            col = 1;
            row += 1;
        }
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
    public ChessPosition getWhiteKingLocation(){
        int row = 1;
        int col = 1;
        int kingRow = 1;
        int kingCol = 1;
        boolean kingFound = false;
            //find location of King
            while (row < 9) {
                while (col < 9) {
                    if (this.board.getPiece(new ChessPosition(row, col)) != null && this.board.getPiece(new ChessPosition(row, col)).getTeamColor() == TeamColor.WHITE){
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
        return new ChessPosition(kingRow, kingCol);
    }

    public ChessPosition getBlackKingLocation(){
        int row = 8;
        int col = 1;
        int kingRow = 1;
        int kingCol = 1;
        boolean kingFound = false;
        while (row > 0) {
            while (col < 9) {
                if (this.board.getPiece(new ChessPosition(row, col)) != null && this.board.getPiece(new ChessPosition(row, col)).getTeamColor() == TeamColor.BLACK) {
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
        return new ChessPosition(kingRow, kingCol);
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
