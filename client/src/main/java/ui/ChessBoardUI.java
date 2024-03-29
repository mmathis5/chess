package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardUI {
    private static Boolean isWhite = true;
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final String EMPTY = " ";
    private static ChessBoard chessBoard = new ChessBoard();
    ChessBoardUI(ChessBoard chessBoard){
        this.chessBoard = chessBoard;
    }

    public void drawBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);

        out.println(1);
        //flipped orientation
        drawHeaders(out);
        drawChessBoardFlipped(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(EMPTY);
        printHeaderText(out, headerText);
        out.print(EMPTY);
        setBlack(out);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(player);
    }

    private static void drawChessBoard(PrintStream out) {
        int row = 8;
        for (int boardRow = 8; boardRow > 0; --boardRow) {
            isWhite = boardRow % 2 == 0;
            drawRowOfSquares(out, row);
            row --;
        }
    }
    private static void drawChessBoardFlipped(PrintStream out) {
        int row = 1;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            isWhite = boardRow % 2 == 0;
            drawRowOfSquares(out, row);
            row ++;
        }
    }

    private static void drawRowOfSquares(PrintStream out, int row) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < 10; ++boardCol) {
                //print the row labels
                if (boardCol == 0 || boardCol == 9){
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(EMPTY);
                    out.print(row);
                    out.print(EMPTY);
                    continue;
                }
                setWhite(out);

                printSquare(out, getPiece(row, boardCol));
                setBlack(out);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.println();
        }
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGray(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static ChessPiece getPiece(int row, int column){
        return chessBoard.getPiece(new ChessPosition(row, column));
    }

    private static void printSquare(PrintStream out, ChessPiece piece) {
        //set square color
        if (isWhite()) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else{
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_RED);
        }
        //get piece type and color
        if (piece != null){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_BLUE);
            }
            else{
                out.print(SET_TEXT_COLOR_RED);
            }
        }
        //get piece type
        String pieceString = " ";
        if (piece != null) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                pieceString = "P";
            }
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                pieceString = "R";
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                pieceString = "N";
            }
            if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                pieceString = "B";
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                pieceString = "K";
            }
            if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                pieceString = "Q";
            }
        }
        out.print(EMPTY);
        out.print(pieceString);
        out.print(EMPTY);
        setWhite(out);
    }

    private static Boolean isWhite(){
        if (isWhite){
            isWhite = false;
            return true;
        }
        else{
            isWhite = true;
            return false;
        }
    }
}

