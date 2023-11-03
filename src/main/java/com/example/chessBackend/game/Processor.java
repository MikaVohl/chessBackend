package com.example.chessBackend.game;

public class Processor {

    private Board boardObject;
    private boolean whiteTurn = true;
    public Processor(){
        boardObject = new Board();
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
