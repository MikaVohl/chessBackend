package com.example.chessBackend.game.pieces;

public class Knight extends Piece {

    public Knight(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard){
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];

        if (row > 1) { // if there is space to move two rows upwards
            if (col > 0 && checkTrue(row - 2, col - 1, currentBoard)) possibleMoves[row - 2][col - 1] = true;
            if (col < 7 && checkTrue(row - 2, col + 1, currentBoard)) possibleMoves[row - 2][col + 1] = true;
        }
        if (row > 0) { // if there is space to move one row upwards
            if (col > 1 && checkTrue(row - 1, col - 2, currentBoard)) possibleMoves[row - 1][col - 2] = true;
            if (col < 6 && checkTrue(row - 1, col + 2, currentBoard)) possibleMoves[row - 1][col + 2] = true;
        }
        if (row < 6) { // if there is space to move two rows downwards
            if (col > 0 && checkTrue(row + 2, col - 1, currentBoard)) possibleMoves[row + 2][col - 1] = true;
            if (col < 7 && checkTrue(row + 2, col + 1, currentBoard)) possibleMoves[row + 2][col + 1] = true;
        }
        if (row < 7) { // if there is space to move two rows downwards
            if (col > 1 && checkTrue(row + 1, col - 2, currentBoard)) possibleMoves[row + 1][col - 2] = true;
            if (col < 6 && checkTrue(row + 1, col + 2, currentBoard)) possibleMoves[row + 1][col + 2] = true;
        }
        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
