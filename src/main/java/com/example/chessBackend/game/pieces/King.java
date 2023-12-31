package com.example.chessBackend.game.pieces;

import com.example.chessBackend.game.Board;

public class King extends Piece {

    public King(boolean isWhite, int row, int col){
        super(isWhite, row, col);
        this.value = 999;
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard) {
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];
        if (row > 0) {
            if (checkTrue(row - 1, col, currentBoard)) possibleMoves[row - 1][col] = true;
            if (col > 0 && checkTrue(row - 1, col - 1, currentBoard)) possibleMoves[row - 1][col - 1] = true;
            if (col < 7 && checkTrue(row - 1, col + 1, currentBoard)) possibleMoves[row - 1][col + 1] = true;
        }
        if (row < 7) {
            if (checkTrue(row + 1, col, currentBoard)) possibleMoves[row + 1][col] = true;
            if (col > 0 && checkTrue(row + 1, col - 1, currentBoard)) possibleMoves[row + 1][col - 1] = true;
            if (col < 7 && checkTrue(row + 1, col + 1, currentBoard)) possibleMoves[row + 1][col + 1] = true;
        }
        if (col > 0 && checkTrue(row, col - 1, currentBoard)) possibleMoves[row][col - 1] = true;
        if (col < 7 && checkTrue(row, col + 1, currentBoard)) possibleMoves[row][col + 1] = true;

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
