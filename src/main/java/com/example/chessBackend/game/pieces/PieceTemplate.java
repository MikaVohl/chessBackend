package com.example.chessBackend.game.pieces;

public interface PieceTemplate {

    boolean isWhite();
    void setWhite(boolean isWhite);
    Piece copy();

    boolean[][] generateMoves(Piece[][] currentBoard); // will generate the pattern of tiles it can generally occupy, this pattern will then be aligned later with its actual position on the board, providing a map of real possible moves
    void setRow(int row);

    void setCol(int col);
    boolean checkTrue(int row, int col, Piece[][] currentBoard);

    int getRow();

    int getCol();

    int getValue();
}
