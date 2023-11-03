package com.example.chessBackend.game.pieces;

public class Queen extends Piece {

    public Queen(boolean isWhite, int row, int col){
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
        for (int i = 1; i < 8; i++) {
            // Up-left diagonal
            if (row - i >= 0 && col - i >= 0) {
                possibleMoves[row - i][col - i] = true;
            }
            // Up-right diagonal
            if (row - i >= 0 && col + i < 8) {
                possibleMoves[row - i][col + i] = true;
            }
            // Down-left diagonal
            if (row + i < 8 && col - i >= 0) {
                possibleMoves[row + i][col - i] = true;
            }
            // Down-right diagonal
            if (row + i < 8 && col + i < 8) {
                possibleMoves[row + i][col + i] = true;
            }
        }

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
