package com.example.chessBackend.game;

public class Board {
    private char[][] chessBoard;

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

        chessBoard = new char[8][8];

    }

    public char[][] getBoardArray() {
        return chessBoard;
    }

    public char getPieceAt(int row, int col){
        if(row > 7 || row < 0 || col > 7 || col < 0) return 'x';
        return chessBoard[row][col];
    }
}
