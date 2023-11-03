package com.example.chessBackend.game;

public class Processor {

    private final static Board boardObject = new Board();
    private static boolean whiteTurn = true;

    public static String fetchPiece(int row, int col){
        System.out.println(row+", "+col);
        if(boardObject.getPieceAt(row, col) == null) return "none";
        return boardObject.getPieceAt(row, col).toString();
    }
//    public String selectSquare(int row, int col){
//        if(Character.isUpperCase(boardObject.getPieceAt(row, col)) == whiteTurn){
//            return generateMoves(row, col);
//        }
//        return null;
//    }
//
//    public String generateMoves(int row, int col){
//
//    }
}
