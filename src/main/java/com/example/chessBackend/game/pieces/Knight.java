package com.example.chessBackend.game.pieces;

import com.example.chessBackend.game.Board;

public class Knight extends Piece {

    public Knight(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard){
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
