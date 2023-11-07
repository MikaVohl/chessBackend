package com.example.chessBackend.game.pieces;

import com.example.chessBackend.game.Board;

public class Bishop extends Piece{

    public Bishop(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard){
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];

        boolean[] scanDone = new boolean[4];

        for (int i = 1; i < 8; i++) {
            // Up-left diagonal
            if (!scanDone[0] && row - i >= 0 && col - i >= 0) {
                if(currentBoard[row-i][col-i] != null){ // if a piece detected in the path
                    if(currentBoard[row-i][col-i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row-i][col-i] = true;
                    }
                    scanDone[0] = true;
                }
                else {
                    possibleMoves[row - i][col - i] = true;
                }
            }
            // Up-right diagonal
            if (!scanDone[1] && row - i >= 0 && col + i < 8) {
                if(currentBoard[row-i][col+i] != null){ // if a piece detected in the path
                    if(currentBoard[row-i][col+i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row-i][col+i] = true;
                    }
                    scanDone[1] = true;
                }
                else {
                    possibleMoves[row - i][col + i] = true;
                }
            }
            // Down-left diagonal
            if (!scanDone[2] && row + i < 8 && col - i >= 0) {
                if(currentBoard[row+i][col-i] != null){ // if a piece detected in the path
                    if(currentBoard[row+i][col-i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row+i][col-i] = true;
                    }
                    scanDone[2] = true;
                }
                else {
                    possibleMoves[row + i][col - i] = true;
                }
            }
            // Down-right diagonal
            if (!scanDone[3] && row + i < 8 && col + i < 8) {
                if(currentBoard[row+i][col+i] != null){ // if a piece detected in the path
                    if(currentBoard[row+i][col+i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row+i][col+i] = true;
                    }
                    scanDone[3] = true;
                }
                else {
                    possibleMoves[row + i][col + i] = true;
                }
            }
        }
        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
