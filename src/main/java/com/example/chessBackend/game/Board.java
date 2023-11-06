package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.*;

public class Board {
    private Piece[][] chessBoard;

    public Board(){
//        chessBoard = new char[][]{
//                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
//                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
//                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
//        };

        chessBoard = new Piece[8][8];
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(i>1 && i<6) break;
                boolean isWhite = true;
                if(i<=1) isWhite = false;
                if(i == 1 || i == 6) chessBoard[i][j] = new Pawn(isWhite, i, j);
                else if(j == 0 || j == 7) chessBoard[i][j] = new Rook(isWhite, i, j);
                else if(j == 1 || j == 6) chessBoard[i][j] = new Knight(isWhite, i, j);
                else if(j == 2 || j == 5) chessBoard[i][j] = new Bishop(isWhite, i, j);
                else if(j == 3) chessBoard[i][j] = new Queen(isWhite, i, j);
                else chessBoard[i][j] = new King(isWhite, i, j);
            }
        }

    }

    public Piece[][] getBoardArray() {
        return chessBoard;
    }

    public Piece getPieceAt(int row, int col){
        if(row > 7 || row < 0 || col > 7 || col < 0) return null;
        return chessBoard[row][col];
    }

    public void setPiece(int row, int col, Piece piece){
        chessBoard[row][col] = piece;
        if(piece != null) {
            piece.setCol(col);
            piece.setRow(row);
        }
    }

    public void movePiece(int row1, int col1, int row2, int col2){
        setPiece(row2, col2, getPieceAt(row1, col1));
    }

    public String decodeBoardIntoImg(){
        StringBuilder output = new StringBuilder();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Piece currPiece = chessBoard[i][j];
                if(currPiece == null) {
                    output.append("  ");
                    continue;
                }
                if(currPiece.isWhite()) output.append("w");
                else output.append("b");
                if(currPiece.toString().equals("Knight")) output.append("n");
                else output.append((char) (currPiece.toString().charAt(0)+32));
            }
        }
        return new String(output);
    }
}
