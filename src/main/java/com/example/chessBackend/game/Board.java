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
//                if(isWhite) {
//
//                }
//                else{
                if (i == 1 || i == 6) chessBoard[i][j] = new Pawn(isWhite, i, j);
                else if (j == 0 || j == 7) chessBoard[i][j] = new Rook(isWhite, i, j);
                else if (j == 1 || j == 6) chessBoard[i][j] = new Knight(isWhite, i, j);
                else if (j == 2 || j == 5) chessBoard[i][j] = new Bishop(isWhite, i, j);
                else if (j == 3) chessBoard[i][j] = new Queen(isWhite, i, j);
                else chessBoard[i][j] = new King(isWhite, i, j);
//                }
            }
        }
//        chessBoard[7][4] = new King(true, 7, 4);
//        chessBoard[5][0] = new Queen(false, 5, 0);
//        chessBoard[6][1] = new Queen(false, 6, 1);

    }

    public Piece[][] getBoardArray() {
        Piece[][] finalBoard = new Piece[8][8];
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(chessBoard[i][j] != null)
                    finalBoard[i][j] = chessBoard[i][j].copy();
            }
//            System.arraycopy(chessBoard[i], 0, finalBoard[i], 0, 8);
        }
        return finalBoard;
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

//    public boolean[][] removeFalseMoves(boolean[][] preCheck, boolean isWhiteTurn){
//        boolean[][] output = cloneArray(preCheck);
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(!preCheck[i][j]) continue;
//                if(chessBoard[i][j] != null && chessBoard[i][j].isWhite() == isWhiteTurn){ // if piece on selected square is of the same color as user
//                    output[i][j] = false;
//                }
//            }
//        }
//        return output;
//    }

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
