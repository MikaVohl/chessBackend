package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.Piece;
import jakarta.websocket.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Processor {

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

        boolean[][] selectionBoard = findSelections(sessionId, row, col);

        sessionVars.setLastSelections(selectionBoard);

        String selectionsString = encodeSelections(selectionBoard);

        sessionVars.setFirstClick(false);
        sessionVars.setFirstClickCoords(row, col);

        return "1"+selectionsString;
    }

    public static String secondClick(String sessionId, int row, int col){
        String output = "";
        SessionVariables sessionVars = getSessionVariables(sessionId);
        if(sessionVars.getLastSelections()[row][col]) { // successful second click
            int firstR = sessionVars.getFirstClickCoords()[0];
            int firstC = sessionVars.getFirstClickCoords()[1];
            sessionVars.getBoardObject().movePiece(firstR, firstC, row, col);
            sessionVars.getBoardObject().setPiece(firstR, firstC, null);

            output = ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());

            sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());

            if(isCheckMate(sessionId, sessionVars.isWhiteTurn())){
                return "3"+sessionVars.getBoardObject().decodeBoardIntoImg();
            }
        }
        else{ // unsuccessful second click
            sessionVars.setFirstClick(true);
            return firstClick(sessionId, row, col);
        }
        sessionVars.setFirstClick(true);
        sessionVars.setLastBoard(output);

        if(sessionVars.isCpuGame()) {
            makeComputerMove(sessionVars.isWhiteTurn(), sessionId);
            sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());
            output = ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());
        }
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

    public static boolean isCheckMate(String sessionId, boolean isCheckingWhite){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Board board = sessionVars.getBoardObject();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pieceHasMoves(sessionId, i, j)) return false;
            }
        }
        return true;
    }

    public static boolean pieceHasMoves(String sessionId, int row, int col){
        boolean[][] possibleMoves = findSelections(sessionId, row, col);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(possibleMoves[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean[][] findSelections(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Board board = sessionVars.getBoardObject();
        Piece selectedPiece = board.getPieceAt(row, col);
        if(selectedPiece == null || selectedPiece.isWhite() != sessionVars.isWhiteTurn()) return new boolean[8][8];

        boolean[][] pieceMoves = selectedPiece.generateMoves(board.getBoardArray());
        revealedChecks(sessionVars.isWhiteTurn(), pieceMoves, board.getBoardArray(), row, col);
        return pieceMoves;
    }
    public static boolean[][] findSelectionsNoChecks(Piece[][] pieceBoard, boolean isWhite, int row, int col){
        Piece selectedPiece = pieceBoard[row][col];
        if(selectedPiece == null || selectedPiece.isWhite() != isWhite) return new boolean[8][8];

        boolean[][] pieceMoves = selectedPiece.generateMoves(pieceBoard);

        return pieceMoves;
    }
    public static void printBoard(Piece[][] board){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j] == null) System.out.print("__");
                else System.out.print(board[i][j].toString().substring(0,2));
                System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printBoard(boolean[][] board){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j]) System.out.print("O");
                else System.out.print("_");
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void revealedChecks(boolean whiteKing, boolean[][] currentSelections, Piece[][] boardCopy, int startRow, int startCol){
//        System.out.println("End");
//        printBoard(currentSelections);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(!currentSelections[i][j]) continue;
                Piece[][] tempBoard = copyPieceBoard(boardCopy);
//                printBoard(tempBoard);
                movePieceCopy(startRow, startCol, i, j, tempBoard);
                setPieceCopy(startRow, startCol, null, tempBoard);
//                printBoard(tempBoard);
                if(isInCheck(whiteKing, tempBoard)){
                    currentSelections[i][j] = false;
                }
            }
        }
//        System.out.println("Beginning");
//        printBoard(currentSelections);

    }
    public static boolean isInCheck(boolean whiteKing, Piece[][] pieceBoard){
        int[] kingCoords = kingLocation(whiteKing, pieceBoard);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(findSelectionsNoChecks(pieceBoard, !whiteKing, i, j)[kingCoords[0]][kingCoords[1]]){
                    return true;
                }
            }
        }
        return false;
    }
    public static int[] kingLocation(boolean whiteKing, Piece[][] pieceBoard){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pieceBoard[i][j] != null && pieceBoard[i][j].toString().equals("King") && pieceBoard[i][j].isWhite() == whiteKing) return new int[]{i,j};
            }
        }
        return new int[2];
    }
    public static Piece[][] copyPieceBoard(Piece[][] referenceBoard){
        Piece[][] outputBoard = new Piece[8][8];
        for(int i=0; i<8; i++){
            System.arraycopy(referenceBoard[i], 0, outputBoard[i], 0, 8);
        }
        return outputBoard;
    }

    public static void setPieceCopy(int row, int col, Piece piece, Piece[][] chessBoardCopy){
        chessBoardCopy[row][col] = piece;
    }

    public static void movePieceCopy(int row1, int col1, int row2, int col2, Piece[][] chessBoardCopy){
        setPieceCopy(row2, col2, chessBoardCopy[row1][col1], chessBoardCopy);
    }

    public static String encodeSelections(boolean[][] moves){
        StringBuilder output = new StringBuilder();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(moves[i][j]) output.append(i).append(j);
            }
        }
        return output.toString();
    }

    public static String generateCode(){
        Random rand = new Random();
        StringBuilder output = new StringBuilder();
        for(int i=0; i<4; i++){
            output.append((char) (rand.nextInt(97, 123)));
        }
        return output.toString();
    }

//    public static void randomMove(boolean isWhite, String sessionId){
//        SessionVariables sessionVars = getSessionVariables(sessionId);
//        boolean[][] possibleMoves = possibleComputerMoves(isWhite, sessionId);
//        Random rand = new Random();
//        while(true){
//            int index = rand.nextInt(0, 64);
//            if(possibleMoves[index/8][index%8]){
//                sessionVars.getBoardObject().movePiece(firstR, firstC, index/8, index%8);
//                sessionVars.getBoardObject().setPiece(firstR, firstC, null);
//                break;
//            }
//        }
//    }
    public static void makeComputerMove(boolean isWhite, String sessionId){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Piece[][] pieceBoard = sessionVars.getBoardObject().getBoardArray();
        Random rand = new Random();
        int index = rand.nextInt(0, 8);
        for(int i=0; i < 8; i++){
            int row = i < 8-index ? (i+index)%8 : i - (8-index);
            for(int j=0; j<8; j++){
                int col = j;
                System.out.println("Row "+row+"  Col "+col);
                if(pieceBoard[row][col] != null && pieceBoard[row][col].isWhite() == isWhite){
                    boolean[][] tempMoves = findSelections(sessionId, row, col);
                    printBoard(tempMoves);
                    int indexPiece = rand.nextInt(0, 8);
                    for(int x=0; x < 8; x++){
                        int currRow = x < 8-indexPiece ? (x+indexPiece)%8 : x - (8-indexPiece);
                        for(int y=0; y<8; y++){
                            int currCol = y;
                            if(tempMoves[currRow][currCol]){
                                System.out.println("HEREEEE");
                                sessionVars.getBoardObject().movePiece(row, col, currRow, currCol);
                                sessionVars.getBoardObject().setPiece(row, col, null);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

//    public static boolean[][] unionSelections(boolean[][] board1, boolean[][] board2){
//        boolean[][] outputBoard = new boolean[8][8];
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(board1[i][j] || board2[i][j]){
//                    outputBoard[i][j] = true;
//                }
//            }
//        }
//        return outputBoard;
//    }
}
