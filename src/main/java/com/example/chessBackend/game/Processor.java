package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.Piece;
import jakarta.websocket.Session;

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
        if(!sessionVariablesMap.containsKey(sessionId)) addSessionId(sessionId);
        return sessionVariablesMap.get(sessionId);
    }

    public static String fetchPiece(String sessionId, int row, int col) {
        Board board = getSessionVariables(sessionId).getBoardObject();
        if (board.getPieceAt(row, col) == null) return "none";
        return board.getPieceAt(row, col).toString();
    }

    public static String firstClick(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        sessionVars.setFirstClick(false);
        sessionVars.setFirstClickCoords(row, col);
        return "1"+findSelections(sessionId, row, col);
    }

    public static String secondClick(String sessionId, int row, int col){
        String output = "";
        SessionVariables sessionVars = getSessionVariables(sessionId);
        if(sessionVars.getLastSelections()[row][col]) { // successful second click
            int firstR = sessionVars.getFirstClickCoords()[0];
            int firstC = sessionVars.getFirstClickCoords()[1];
            sessionVars.getBoardObject().movePiece(firstR, firstC, row, col);
            sessionVars.getBoardObject().setPiece(firstR, firstC, null);

            output+= ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());

            sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());
        }
        else{ // unsuccessful second click
            return firstClick(sessionId, row, col);
        }
        sessionVars.setFirstClick(true);
        sessionVars.setLastBoard(output);
        return output;
    }

    public static String processClick(String sessionId, int row, int col){
        SessionVariables sessionVars = Processor.getSessionVariables(sessionId);
        String output = "";

        if(sessionVars.getFirstClick())
            output += Processor.firstClick(sessionId, row, col);
        else
            output+= Processor.secondClick(sessionId, row, col);

        return output;
    }

    public static String findSelections(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Board board = sessionVars.getBoardObject();
        Piece selectedPiece = board.getPieceAt(row, col);
        if(selectedPiece == null || selectedPiece.isWhite() != sessionVars.isWhiteTurn()) return "";
        boolean[][] moves = selectedPiece.generateMoves();
        sessionVars.setLastSelections(moves);

        StringBuilder output = new StringBuilder();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(moves[i][j]) output.append(i).append(j);
            }
        }
        return output.toString();
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
