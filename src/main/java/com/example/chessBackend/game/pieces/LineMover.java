package com.example.chessBackend.game.pieces;

public abstract class LineMover extends Piece {
    int range = 0;

    LineMover(boolean isWhite, int row, int col, int range){
        super(isWhite, row, col);
        this.range = range;
    }

    @Override
    public String generateMoves(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++)
        }
        return null;
    }
}
