package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.Piece;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Processor {

//    private final static Board boardObject = new Board();
//    private static boolean whiteTurn = true;
//    private static boolean firstClick = true;
    private static final Map<String, SessionVariables> sessionVariablesMap = new HashMap<>();

    public static void addSessionId(String sessionId){
        sessionVariablesMap.put(sessionId, new SessionVariables(sessionId));
    }

    public static SessionVariables getSessionVariables(String sessionId){
        return sessionVariablesMap.get(sessionId);
    }

    public static String fetchPiece(String sessionId, int row, int col){
        Board board = getSessionVariables(sessionId).getBoardObject();
        if(board.getPieceAt(row, col) == null) return "none";
        return board.getPieceAt(row, col).toString();
    }

    public static boolean isFirstClick(String sessionId) { return getSessionVariables(sessionId).getFirstClick(); }

    public static String findSelections(String sessionId, int row, int col){
        Board board = getSessionVariables(sessionId).getBoardObject();
        Piece selectedPiece = board.getPieceAt(row, col);
        if(selectedPiece == null) return "";
        boolean[][] moves = selectedPiece.generateMoves();

        String output = "";
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(moves[i][j]) output += i+""+j;
            }
        }
        return output;
    }
//    public String selectSquare(int row, int col){
//        if(Character.isUpperCase(boardObject.getPieceAt(row, col)) == whiteTurn){
//            return generateMoves(row, col);
//        }
//        return null;
//    }
//
//    public String generateMoves(int row, int col){
//
//    }
}
