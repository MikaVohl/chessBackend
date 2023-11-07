package com.example.chessBackend.game.pieces;

import com.example.chessBackend.game.Board;

public class Queen extends Piece {

    public Queen(boolean isWhite, int row, int col){
        super(isWhite, row, col);
    }

    @Override
    public boolean[][] generateMoves(Piece[][] currentBoard){
        int row = getRow();
        int col = getCol();
        boolean[][] possibleMoves = new boolean[8][8];
        for(int i = row+1; i<=7; i++){ // scan downwards
            if(currentBoard[i][col] != null){ // if a piece detected in the path
                if(currentBoard[i][col].isWhite() != isWhite()){ // if pieces are different colour
                    possibleMoves[i][col] = true;
                }
                break;
            }
            possibleMoves[i][col] = true;
        }
        for(int i = row-1; i>=0; i--){ // scan upwards
            if(currentBoard[i][col] != null){ // if a piece detected in the path
                if(currentBoard[i][col].isWhite() != isWhite()){ // if pieces are different colour
                    possibleMoves[i][col] = true;
                }
                break;
            }
            possibleMoves[i][col] = true;
        }
        for(int i = row+1; i<=7; i++){ // scan right
            if(currentBoard[row][i] != null){ // if a piece detected in the path
                if(currentBoard[row][i].isWhite() != isWhite()){ // if pieces are different colour
                    possibleMoves[row][i] = true;
                }
                break;
            }
            possibleMoves[row][i] = true;
        }
        for(int i = col-1; i>=0; i--){ // scan left
            if(currentBoard[row][i] != null){ // if a piece detected in the path
                if(currentBoard[row][i].isWhite() != isWhite()){ // if pieces are different colour
                    possibleMoves[row][i] = true;
                }
                break;
            }
            possibleMoves[row][i] = true;
        }
        boolean[] scanDone = new boolean[4];

        for (int i = 1; i < 8; i++) {
            // Up-left diagonal
            if (!scanDone[0] && row - i >= 0 && col - i >= 0) {
                if(currentBoard[row-i][col-i] != null){ // if a piece detected in the path
                    if(currentBoard[row-i][col-i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row-i][col-i] = true;
                    }
                    scanDone[0] = true;
                }
                else {
                    possibleMoves[row - i][col - i] = true;
                }
            }
            // Up-right diagonal
            if (!scanDone[1] && row - i >= 0 && col + i < 8) {
                if(currentBoard[row-i][col+i] != null){ // if a piece detected in the path
                    if(currentBoard[row-i][col+i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row-i][col+i] = true;
                    }
                    scanDone[1] = true;
                }
                else {
                    possibleMoves[row - i][col + i] = true;
                }
            }
            // Down-left diagonal
            if (!scanDone[2] && row + i < 8 && col - i >= 0) {
                if(currentBoard[row+i][col-i] != null){ // if a piece detected in the path
                    if(currentBoard[row+i][col-i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row+i][col-i] = true;
                    }
                    scanDone[2] = true;
                }
                else {
                    possibleMoves[row + i][col - i] = true;
                }
            }
            // Down-right diagonal
            if (!scanDone[3] && row + i < 8 && col + i < 8) {
                if(currentBoard[row+i][col+i] != null){ // if a piece detected in the path
                    if(currentBoard[row+i][col+i].isWhite() != isWhite()){ // if pieces are different colour
                        possibleMoves[row+i][col+i] = true;
                    }
                    scanDone[3] = true;
                }
                else {
                    possibleMoves[row + i][col + i] = true;
                }
            }
        }

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
