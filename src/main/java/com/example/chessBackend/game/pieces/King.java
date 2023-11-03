package com.example.chessBackend.game.pieces;

public class King extends Piece {

    public King(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(){
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];
        if(row > 0){
            possibleMoves[row-1][col] = true;
            if(col > 0) possibleMoves[row-1][col-1] = true;
            if(col < 7) possibleMoves[row-1][col+1] = true;
        }
        if(row < 7){
            possibleMoves[row+1][col] = true;
            if(col > 0) possibleMoves[row+1][col-1] = true;
            if(col < 7) possibleMoves[row+1][col+1] = true;
        }
        if(col > 0) possibleMoves[row][col-1] = true;
        if(col < 7) possibleMoves[row][col+1] = true;

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
