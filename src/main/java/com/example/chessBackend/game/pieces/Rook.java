package com.example.chessBackend.game.pieces;

import com.example.chessBackend.game.Board;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int col){
        super(isWhite, row, col);
        this.value = 4;
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
        for(int i = col+1; i<=7; i++){ // scan right
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

        possibleMoves[row][col] = false;
        return possibleMoves;
    }
}
