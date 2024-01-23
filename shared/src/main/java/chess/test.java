package chess;

public class test {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.addPiece(new ChessPosition(5, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        System.out.println(board.getPiece(new ChessPosition(5, 4)).pieceMoves(board, new ChessPosition(5, 4)));
//        for (int i = 1 ; i < 9 ; i++){
//            for (int j = 1; j < 9 ; j++){
//                System.out.println(board.getPiece(new ChessPosition(i, j)));
 //           }
 //       }
    }
}
