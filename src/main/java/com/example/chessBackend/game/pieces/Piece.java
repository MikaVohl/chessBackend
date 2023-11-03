package com.example.chessBackend.game.pieces;

public abstract class Piece implements PieceTemplate{
    private boolean isWhite = true;
    private int row = 0;
    private int col = 0;

    public Piece(boolean isWhite, int row, int col){
        setWhite(isWhite);
        setRow(row);
        setCol(col);
    }
    @Override
    public boolean isWhite() {
        return this.isWhite;
    }

    @Override
    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getCol() {
        return this.col;
    }
}
