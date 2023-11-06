package com.example.chessBackend.game;

public class SessionVariables {

    private boolean whiteTurn = true;
    private boolean firstClick = true;
    private String id = null;
    private Board boardObject = null;

    public SessionVariables(String id){
        this.id = id;
        boardObject = new Board();
    }

    public Board getBoardObject() {
        return boardObject;
    }

    public boolean getFirstClick(){
        return firstClick;
    }
}
