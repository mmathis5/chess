package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Queen;
import chess.pieces.Knight;
import chess.pieces.Rook;
import chess.pieces.Pawn;





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
        //don't code a class for queen, just combine the moves from rook and bishop.
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            moves.addAll(new Bishop(board, myPosition, this.team).pieceMovesBishop());
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.ROOK){
            moves.addAll(new Rook(board, myPosition, this.team).pieceMovesRook());
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
            moves.addAll(new Knight(board, myPosition, this.team).pieceMovesKnight());
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.KING){
            moves.addAll(new King(board, myPosition, this.team).pieceMovesKing());
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            moves.addAll(new Rook(board, myPosition, this.team).pieceMovesRook());
            moves.addAll(new Bishop(board, myPosition, this.team).pieceMovesBishop());
        }
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
            moves.addAll(new Pawn(board, myPosition, this.team).pieceMovesPawn());
        }
        return moves;
    }
}
