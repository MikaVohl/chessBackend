package com.example.chessBackend.game;

import java.util.HashMap;
import java.util.Map;

public class SessionVariables {
    private boolean whiteTurn = true;
    private boolean firstClick = true;
    private String id = null;
    private Board boardObject = null;
    private boolean[][] lastSelections = null;
    private String lastBoard = null;
    private int[] firstClickCoords = null;
    private int movesMade = 0;
    private HashMap<String, Integer> whitePieces = null;
    private HashMap<String, Integer> blackPieces = null;

    public SessionVariables(String id){
        this.id = id;
        boardObject = new Board();
        firstClick = true;
        whiteTurn = true;
        whitePieces = generatePieces(new HashMap<>());
        blackPieces = generatePieces(new HashMap<>());
    }

    public HashMap<String, Integer> generatePieces(HashMap<String, Integer> pieceMap){
        pieceMap.put("Pawn", 8);
        pieceMap.put("Rook", 2);
        pieceMap.put("Knight", 2);
        pieceMap.put("Bishop", 2);
        pieceMap.put("Queen", 1);
        pieceMap.put("King", 1);
        return pieceMap;
    }
    public int blackPiecesLeft(){
        int count = 0;
        for(Map.Entry<String, Integer> entry : blackPieces.entrySet()){
            count += entry.getValue();
        }
        return count;
    }
    public int whitePiecesLeft() {
        int count = 0;
        for (Map.Entry<String, Integer> entry : whitePieces.entrySet()) {
            count += entry.getValue();
        }
        return count;
    }

    public void addPiece(String piece, boolean isWhite){
        if(isWhite){
            whitePieces.put(piece, whitePieces.get(piece)+1);
        } else {
            blackPieces.put(piece, blackPieces.get(piece)+1);
        }
    }
    public void removePiece(String piece, boolean isWhite){
        if(isWhite){
            whitePieces.put(piece, whitePieces.get(piece)-1);
        } else {
            blackPieces.put(piece, blackPieces.get(piece)-1);
        }
    }
    public Board getBoardObject() {
        return boardObject;
    }

    public void setBoardObject(Board boardObject) {
        this.boardObject = boardObject;
    }

    public void addMovesMade(){ movesMade++;}
    public int getMovesMade(){ return movesMade; }
    public boolean getFirstClick(){
        return firstClick;
    }

    public void setFirstClick(boolean isFirst){
        firstClick = isFirst;
    }
    public void setLastBoard(String board) { lastBoard = board; }
    public String getLastBoard() { return lastBoard; }

    public void setLastSelections(boolean[][] selections){
        lastSelections = selections;
    }

    public boolean[][] getLastSelections(){
        return lastSelections;
    }

    public void setFirstClickCoords(int row, int col){
        firstClickCoords = new int[]{row, col};
    }

    public int[] getFirstClickCoords(){
        return firstClickCoords;
    }
    public boolean isWhiteTurn(){
        return whiteTurn;
    }
    public void setWhiteTurn(boolean isWhite){
        whiteTurn = isWhite;
    }

}
