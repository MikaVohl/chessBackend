package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.Piece;

import java.util.Arrays;

public class Processor {

    private final static Board boardObject = new Board();
    private static boolean whiteTurn = true;
    private static boolean firstClick = true;

    public static String fetchPiece(int row, int col){
        if(boardObject.getPieceAt(row, col) == null) return "none";
        return boardObject.getPieceAt(row, col).toString();
    }

    public static boolean isFirstClick() { return firstClick; }

    public static String findSelections(int row, int col){
        Piece selectedPiece = boardObject.getPieceAt(row, col);
        if(selectedPiece == null) return "";
        boolean[][] moves = selectedPiece.generateMoves();

        String output = "";
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(moves[i][j]) output += i+""+j;
            }
        }
        return output;
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
