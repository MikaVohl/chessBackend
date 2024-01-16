package com.example.chessBackend.game.pieces;

public class Pawn extends Piece{

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
        this.value = 1;
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard) {
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];
        if(row > 0 && isWhite()){
            if(checkTrue(row-1,col,currentBoard)) possibleMoves[row-1][col] = true;
            if(col > 0 && checkTrueTake(row-1,col-1,currentBoard)) possibleMoves[row-1][col-1] = true;
            if(col < 7 && checkTrueTake(row-1,col+1,currentBoard)) possibleMoves[row-1][col+1] = true;
            if(row == 6 && checkTrue(row-2,col,currentBoard) && checkTrue(row-1, col, currentBoard)) possibleMoves[row-2][col] = true;
        }
        else if(row < 7 && !isWhite()){
            if(checkTrue(row+1,col,currentBoard)) possibleMoves[row+1][col] = true;
            if(col > 0 && checkTrueTake(row+1,col-1,currentBoard)) possibleMoves[row+1][col-1] = true;
            if(col < 7 && checkTrueTake(row+1,col+1,currentBoard)) possibleMoves[row+1][col+1] = true;
            if(row == 1 && checkTrue(row+2,col,currentBoard) && checkTrue(row+1, col, currentBoard)) possibleMoves[row+2][col] = true;
        }

        possibleMoves[row][col] = false;

        return possibleMoves;
    }

    public boolean checkTrueTake(int row, int col, Piece[][] currentBoard) {
        return currentBoard[row][col] != null && currentBoard[row][col].isWhite() != isWhite();
    }

    @Override
    public boolean checkTrue(int row, int col, Piece[][] currentBoard) {
        return currentBoard[row][col] == null;
    }

}
