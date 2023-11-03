package com.example.chessBackend.game.pieces;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(){
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];
        for(int i=0; i<8; i++){
            possibleMoves[row][i] = true;
            possibleMoves[i][col] = true;
        }

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
