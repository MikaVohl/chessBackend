package com.example.chessBackend.game;

public class SessionVariables {

    private boolean whiteTurn = true;
    private boolean firstClick = true;
    private String id = null;
    private Board boardObject = null;
    private boolean[][] lastSelections = null;
    private String lastBoard = null;
    private int[] firstClickCoords = null;
    private boolean cpuGame = false;

    public SessionVariables(String id){
        this.id = id;
        boardObject = new Board();
        firstClick = true;
        whiteTurn = true;
    }

    public Board getBoardObject() {
        return boardObject;
    }

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
    public void setCpuGame(boolean isCpuGame) { cpuGame = isCpuGame; }
    public boolean isCpuGame() { return cpuGame; }
}
