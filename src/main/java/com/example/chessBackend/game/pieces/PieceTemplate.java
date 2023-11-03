package com.example.chessBackend.game.pieces;

public interface PieceTemplate {

    boolean isWhite();
    void setWhite(boolean isWhite);

    boolean[][] generateMoves(); // will generate the pattern of tiles it can generally occupy, this pattern will then be aligned later with its actual position on the board, providing a map of real possible moves
    void setRow(int row);

    void setCol(int col);

    int getRow();

    int getCol();

}
