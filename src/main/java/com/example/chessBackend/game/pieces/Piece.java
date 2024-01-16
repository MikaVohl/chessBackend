package com.example.chessBackend.game.pieces;

public abstract class Piece implements PieceTemplate{
    private boolean isWhite = true;
    private int row = 0;
    private int col = 0;
    protected int value = 1;

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
    public Piece copy(){
        switch (toString()){
            case "Pawn":
                return new Pawn(isWhite(), getRow(), getCol());
            case "Rook":
                return new Rook(isWhite(), getRow(), getCol());
            case "Knight":
                return new Knight(isWhite(), getRow(), getCol());
            case "Bishop":
                return new Bishop(isWhite(), getRow(), getCol());
            case "Queen":
                return new Queen(isWhite(), getRow(), getCol());
            case "King":
                return new King(isWhite(), getRow(), getCol());
        }
        return null;
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    @Override
    public int getValue(){
        return this.value;
    }
    @Override
    public boolean checkTrue(int row, int col, Piece[][] currentBoard){
        return currentBoard[row][col] == null || (currentBoard[row][col] != null && currentBoard[row][col].isWhite() != isWhite());
    }
}
