package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.*;

import java.util.*;


public class Processor {

    private final Map<String, SessionVariables> sessionVariablesMap = new HashMap<>();

    public void addSessionId(String sessionId){
        sessionVariablesMap.put(sessionId, new SessionVariables(sessionId));
    }

    public SessionVariables getSessionVariables(String sessionId){
        if(!sessionVariablesMap.containsKey(sessionId)){
//            System.out.println("Session ID not found, adding new session ID: "+sessionId);
            addSessionId(sessionId);
        }
        return sessionVariablesMap.get(sessionId);
    }

    public String fetchPiece(String sessionId, int row, int col) {
        Board board = getSessionVariables(sessionId).getBoardObject();
        if (board.getPieceAt(row, col) == null) return "none";
        return board.getPieceAt(row, col).toString();
    }

    public String firstClick(String sessionId, int row, int col){
//        Piece[][] testBoard = copyPieceBoard(getSessionVariables(sessionId).getBoardObject().getBoardArray());
//        testBoard[7][0] = testBoard[5][0];
//        testBoard[5][0] = null;
//        testBoard[7][0].setRow(7);
//        testBoard[7][0].setCol(0);
//        System.out.println("Test board checkmate "+isCheckMate(testBoard, true));
//        System.out.println(pieceHasMoves(testBoard, 7, 4));
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(testBoard[i][j] != null){
//                    System.out.print(testBoard[i][j].toString().charAt(0));
//                }
//                else{
//                    System.out.print("_");
//                }
//            }
//            System.out.println();
//        }
//        Piece[][] testBoard = new Piece[8][8];
//        testBoard[0][0] = new Rook(false, 0, 0);
//        testBoard[0][7] = new Rook(false, 0, 7);
//        testBoard[2][2] = new Knight(false, 2, 2);
//        testBoard[6][3] = new Knight(false, 6, 3);
//        testBoard[7][0] = new Rook(true, 7, 0);
//        testBoard[7][7] = new Rook(true, 7, 7);
//        testBoard[7][1] = new Knight(true, 7, 1);
//        testBoard[7][6] = new Knight(true, 7, 6);
//        testBoard[0][4] = new King(false, 0, 4);
//        testBoard[4][2] = new King(true, 4, 2);
//        testBoard[1][0] = new Pawn(false, 1, 0);
//        testBoard[1][1] = new Pawn(false, 1, 1);
//        testBoard[1][2] = new Pawn(false, 1, 2);
//        testBoard[1][3] = new Pawn(false, 1, 3);
//        testBoard[1][4] = new Pawn(false, 1, 4);
//        testBoard[1][5] = new Pawn(false, 1, 5);
//        testBoard[1][6] = new Pawn(false, 1, 6);
//        testBoard[1][7] = new Pawn(false, 1, 7);
//        testBoard[6][0] = new Pawn(true, 6, 0);
//        testBoard[6][1] = new Pawn(true, 6, 1);
//        testBoard[6][2] = new Pawn(true, 6, 2);
//        testBoard[6][5] = new Pawn(true, 6, 5);
//        testBoard[6][6] = new Pawn(true, 6, 6);
//        testBoard[6][7] = new Pawn(true, 6, 7);
//        testBoard[0][2] = new Bishop(false, 0, 2);
//        testBoard[0][3] = new Queen(false, 0, 3);
//        testBoard[0][5] = new Bishop(false, 0, 5);
//        testBoard[7][2] = new Bishop(true, 7, 2);
//        testBoard[7][3] = new Queen(true, 7, 3);
//        testBoard[7][5] = new Bishop(true, 7, 5);

//        System.out.println();
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(testBoard[i][j] != null){
//                    System.out.print(testBoard[i][j].toString().charAt(0));
//                }
//                else{
//                    System.out.print("_");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println("white king in check on test board "+isInCheck(true, testBoard));
//        System.out.println("black king in check on test board "+isInCheck(false, testBoard));
//        System.out.println("White king location is "+kingLocation(true, testBoard)[0]+","+kingLocation(true, testBoard)[1]);
//        System.out.println("Black king location is "+kingLocation(false, testBoard)[0]+","+kingLocation(false, testBoard)[1]);
        // make the board as follows:
        /*
        R_BQKB_R
        PPPPPPPP
        __K_____
        ________
        __K_____
        ________
        PPPK_PPP
        RKBQ_BKR
         */


        SessionVariables sessionVars = getSessionVariables(sessionId);

        boolean[][] selectionBoard = findSelections(sessionId, row, col);

        sessionVars.setLastSelections(selectionBoard);

        String selectionsString = encodeSelections(selectionBoard);

        sessionVars.setFirstClick(false);
        sessionVars.setFirstClickCoords(row, col);

        return "1"+selectionsString;
    }

    public String secondClick(String sessionId, int row, int col){
        String output = "";
        SessionVariables sessionVars = getSessionVariables(sessionId);
        if(sessionVars.getLastSelections()[row][col]) { // successful second click
            int firstR = sessionVars.getFirstClickCoords()[0];
            int firstC = sessionVars.getFirstClickCoords()[1];
            Piece removedPiece = sessionVars.getBoardObject().getPieceAt(row, col);
            if(removedPiece != null) {
                sessionVars.removePiece(removedPiece.toString(), removedPiece.isWhite());
            }
            sessionVars.getBoardObject().movePiece(firstR, firstC, row, col);
            sessionVars.getBoardObject().setPiece(firstR, firstC, null);
            sessionVars.addMovesMade();
//            System.out.println("Position valued at: "+evaluatePosition(sessionVars.getBoardObject().getBoardArray()));

            output = ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());

            sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());

            if(isCheckMate(sessionVars.getBoardObject().getBoardArray(), sessionVars.isWhiteTurn())){
                if(isInCheck(sessionVars.isWhiteTurn(), sessionVars.getBoardObject().getBoardArray())){ // checkmate case
                    return "3"+sessionVars.getBoardObject().decodeBoardIntoImg();
                }
                return "4"+sessionVars.getBoardObject().decodeBoardIntoImg(); // stalemate case
            }
        }
        else{ // unsuccessful second click
            sessionVars.setFirstClick(true);
            return firstClick(sessionId, row, col);
        }
        sessionVars.setFirstClick(true);
        sessionVars.setLastBoard(output);

        // makeComputerMove(sessionVars.isWhiteTurn(), sessionId);
        // sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());
        output = ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());
        return output;
    }

    public String computerTurn(String sessionId){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        makeComputerMove(sessionVars.isWhiteTurn(), sessionId, Integer.MIN_VALUE, Integer.MAX_VALUE);
        sessionVars.addMovesMade();
        sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());
        if(isCheckMate(sessionVars.getBoardObject().getBoardArray(), sessionVars.isWhiteTurn())){
            if(isInCheck(sessionVars.isWhiteTurn(), sessionVars.getBoardObject().getBoardArray())){ // checkmate case
                return "3"+sessionVars.getBoardObject().decodeBoardIntoImg();
            }
            return "4"+sessionVars.getBoardObject().decodeBoardIntoImg(); // stalemate case
        }
        return "5"+sessionVars.getBoardObject().decodeBoardIntoImg();
    }


    public String processClick(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        String output = "";

        if(sessionVars.getFirstClick())
            output += firstClick(sessionId, row, col);
        else
            output+= secondClick(sessionId, row, col);

        return output;
    }

    public boolean isCheckMate(Piece[][] pieceBoard, boolean kingSide){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pieceBoard[i][j] != null && pieceBoard[i][j].isWhite() == kingSide && pieceHasMoves(pieceBoard, i, j)){
//                    System.out.println("Piece at "+i+","+j+" has moves");
//                    System.out.println(pieceBoard[i][j].isWhite()+" "+pieceBoard[i][j].toString());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean kingInCheck(Piece[][] boardArray, boolean isCheckingWhite){
        int[] kingCoords = kingLocation(isCheckingWhite, boardArray);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(boardArray[i][j] != null && boardArray[i][j].isWhite() != isCheckingWhite){ // if enemy of king being checked
                    boolean[][] tempMoves = boardArray[i][j].generateMoves(boardArray);
                    if(tempMoves[kingCoords[0]][kingCoords[1]]) return true;
                }
            }
        }
//        int[] kingCoords = kingLocation(whiteKing, pieceBoard);
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(findSelectionsNoChecks(boardArray, !isCheckingWhite, i, j)[kingCoords[0]][kingCoords[1]]){
//                    return true;
//                }
//            }
//        }
//        return false;
//        boolean result = findSelectionsNoChecks(boardArray, !isCheckingWhite, kingCoords[0], kingCoords[1])[kingCoords[0]][kingCoords[1]];
        return false;
    }

    public boolean pieceHasMoves(Piece[][] pieceBoard, int row, int col){
        boolean[][] possibleMoves = findSelectionsFromBoard(row, col, pieceBoard);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(possibleMoves[i][j]){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean[][] findSelections(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Board board = sessionVars.getBoardObject();
        if(board.getPieceAt(row, col) == null || board.getPieceAt(row, col).isWhite() != sessionVars.isWhiteTurn()) return new boolean[8][8];
        return findSelectionsFromBoard(row, col, board.getBoardArray());
    }

    public boolean[][] findSelectionsFromBoard(int row, int col, Piece[][] pieceBoard){
//        Board boardObj = sessionVars.getBoardObject();
//        Piece selectedPiece = boardObj.getPieceAt(row, col);
        Piece selectedPiece = pieceBoard[row][col];
        if(selectedPiece == null) return new boolean[8][8];
        boolean[][] pieceMoves = selectedPiece.generateMoves(pieceBoard);
        revealedChecks(selectedPiece.isWhite(), pieceMoves, pieceBoard, row, col);
        return pieceMoves;
    }

    // Does not sift out moves that would put the king in check
    public boolean[][] findSelectionsNoChecks(Piece[][] pieceBoard, boolean isWhite, int row, int col){
        final Piece selectedPiece = pieceBoard[row][col];
        if(selectedPiece == null || selectedPiece.isWhite() != isWhite) return new boolean[8][8];

        return selectedPiece.generateMoves(pieceBoard);
    }
    public void printBoard(Piece[][] board){
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

    public void printBoard(boolean[][] board){
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

    public void revealedChecks(boolean whiteKing, boolean[][] currentSelections, Piece[][] boardCopy, int startRow, int startCol){ // removes moves that reveal checks
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
    public boolean isInCheck(boolean whiteKing, Piece[][] pieceBoard){
        int[] kingCoords = kingLocation(whiteKing, pieceBoard);
//        System.out.println("king coords: "+kingCoords[0]+","+kingCoords[1]);
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(findSelectionsNoChecks(pieceBoard, !whiteKing, i, j)[kingCoords[0]][kingCoords[1]]){
                    return true;
                }
            }
        }
        return false;
    }
    public int[] kingLocation(boolean whiteKing, Piece[][] pieceBoard){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pieceBoard[i][j] != null && pieceBoard[i][j].toString().equals("King") && pieceBoard[i][j].isWhite() == whiteKing) return new int[]{i,j};
            }
        }
        return new int[2];
    }
    public Piece[][] copyPieceBoard(Piece[][] referenceBoard){
        Piece[][] outputBoard = new Piece[8][8];
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(referenceBoard[i][j] != null)
                    outputBoard[i][j] = referenceBoard[i][j].copy();
            }
//            System.arraycopy(referenceBoard[i], 0, outputBoard[i], 0, 8);
        }
        return outputBoard;
    }

    public void setPieceCopy(int row, int col, Piece piece, Piece[][] chessBoardCopy){
        chessBoardCopy[row][col] = piece;
        if(piece != null) {
            piece.setCol(col);
            piece.setRow(row);
        }
    }

    public void movePieceCopy(int row1, int col1, int row2, int col2, Piece[][] chessBoardCopy){
        setPieceCopy(row2, col2, chessBoardCopy[row1][col1], chessBoardCopy);
    }

    public String encodeSelections(boolean[][] moves){
        StringBuilder output = new StringBuilder();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(moves[i][j]) output.append(i).append(j);
            }
        }
        return output.toString();
    }

    public String generateCode(){
        Random rand = new Random();
        StringBuilder output = new StringBuilder();
        for(int i=0; i<4; i++){
            output.append((char) (rand.nextInt(97, 123)));
        }
        return output.toString();
    }

    // public static void randomMove(boolean isWhite, String sessionId){
    //     SessionVariables sessionVars = getSessionVariables(sessionId);
    //     Piece[][] pieceBoard = sessionVars.getBoardObject().getBoardArray();
    //     Random rand = new Random();
    //     ArrayList<int[]> validMoves = new ArrayList<>();

    //     // Collect all valid moves
    //     for (int i = 0; i < 8; i++) {
    //         for (int j = 0; j < 8; j++) {
    //             if (pieceBoard[i][j] != null && pieceBoard[i][j].isWhite() == isWhite) {
    //                 boolean[][] tempMoves = findSelections(sessionId, i, j);
    //                 for (int x = 0; x < 8; x++) {
    //                     for (int y = 0; y < 8; y++) {
    //                         if (tempMoves[x][y]) {
    //                             validMoves.add(new int[]{i, j, x, y});
    //                         }
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //     // Choose a random move from the list of valid moves
    //     if (!validMoves.isEmpty()) {
    //         int[] move = validMoves.get(rand.nextInt(validMoves.size()));
    //         sessionVars.getBoardObject().movePiece(move[0], move[1], move[2], move[3]);
    //         sessionVars.getBoardObject().setPiece(move[0], move[1], null);
    //     }
    // }
    public int evaluatePosition(Piece[][] pieceBoard, int movesMade, boolean turn){
        if(!checkValidityBoard(pieceBoard)) System.out.println("BOARD NOT VALID!!!");
        int score = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                final Piece currPiece = pieceBoard[i][j];
                if(currPiece == null) continue;
                int values = (15 * currPiece.getValue());
                values += Math.max(0, (int) (addSquareScore(i, j) - (0.5 * ((float) Math.sqrt(movesMade)))));
//                System.out.println("added "+Math.max(0, (int) (addSquareScore(i, j) - (0.5 * ((float) Math.sqrt(movesMade))))));
//                if(i == 2 && j == 0){
//                    values += 10000;
//                }
//                System.out.println("added "+ (int) (5.0 * addSquareScore(i, j) / ((float) movesMade)));
                if(currPiece.isWhite()){
                    score += values;
                }
                else {
                    score -= values;
                }
            }
        }
//        if(turn){
//            System.out.println("evaluating a move by white");
//        System.out.println();
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if(pieceBoard[i][j] != null){
//                    System.out.print(pieceBoard[i][j].toString().charAt(0));
//                }
//                else{
//                    System.out.print("_");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println("white king in check on test board "+isInCheck(true, pieceBoard));
//        System.out.println("black king in check on test board "+isInCheck(false, pieceBoard));
//        boolean whiteCheckMate = isCheckMate(pieceBoard, true);
//        if(checkMate) System.out.println("Checkmate/stalemate opportunity");
        if(turn) {
            boolean blackCheckMate = isCheckMate(pieceBoard, false);
            if (isInCheck(false, pieceBoard)) {
//            System.out.println("black king in check");
                if(blackCheckMate){
//                    System.out.println("Sees checkmate on black");
//                    System.out.println("returning early");
                    return Integer.MAX_VALUE - 10000;
                }
                score += 14;
            }
            else{
                if(blackCheckMate){
//                    System.out.println("Sees stalemate");
                    score -= 10; // stalemate
                }
            }
        }
        else{
            boolean whiteCheckMate = isCheckMate(pieceBoard, true);
            if(isInCheck(true, pieceBoard)){
//            System.out.println("white king in check");
                if(whiteCheckMate){
//                    System.out.println("Sees checkmate on white");
//                    System.out.println("returning early");
                    return Integer.MIN_VALUE + 10000;
                }
                score -= 14;
            }
            else{
                if(whiteCheckMate){
//                    System.out.println("Sees stalemate");
                    score += 10; // stalemate
                }
            }
        }
//        }
//        else{
////            System.out.println("evaluating a move by black");
//            if(kingInCheck(pieceBoard, true)){
//                System.out.println("white king in check");
//                score -= 10000;
//            }
//            if(kingInCheck(pieceBoard, false)){
//                System.out.println("black king in check");
//                score += 10000;
//            }
//        }
        return score;
    }

    public boolean checkValidityBoard(Piece[][] board){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(board[i][j] != null){
                    if(board[i][j].getRow() != i || board[i][j].getCol() != j){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void makeComputerMove(boolean isWhite, String sessionId, int alpha, int beta){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        int foresightValue = 2;// + (12/(sessionVars.blackPiecesLeft()+sessionVars.whitePiecesLeft()));
//        System.out.println("Depth: "+foresightValue);
//        System.out.println("calculation: "+(12.0/(sessionVars.blackPiecesLeft()+sessionVars.whitePiecesLeft())));
        Piece[][] pieceBoard = sessionVars.getBoardObject().getBoardArray();
//        ArrayList<int[]> validMoves = new ArrayList<>();
        int bestScore = 0;
        int[] bestMove = null;
        boolean setMove = false;

        // Collect all valid moves and evaluate them
        outerloop:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieceBoard[i][j] == null || pieceBoard[i][j].isWhite() != isWhite) continue;
                boolean[][] tempMoves = findSelections(sessionId, i, j);
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (tempMoves[x][y]) {
                            int[] move = new int[]{i, j, x, y};
//                            validMoves.add(move);

                            // Evaluate the move

                            int score = evaluateMove(pieceBoard, move, foresightValue, alpha, beta, sessionVars.getMovesMade());
//                            System.out.println("evaluating move "+i+","+j+" to "+x+","+y+"  score: "+score);
//                            System.out.println("white king in check on test board "+kingInCheck(pieceBoard, true));
//                            System.out.println("black king in check on test board "+kingInCheck(pieceBoard, false));
//                            System.out.println(score);
//                            int score = evaluateMove(pieceBoard, move, 2);
//                             System.out.println(Arrays.toString(move) + ": " + score);
                            if(!setMove){
                                bestMove = move;
                                bestScore = score;
                                setMove = true;
                            }
                            if(isWhite) {
                                if (score > bestScore) {
                                    bestScore = score;
                                    bestMove = move;
                                }
                                if(score > alpha) alpha = score;
                                if(beta <= alpha){
                                    bestMove = move;
                                    break outerloop;
                                }
                            }
                            else {
                                if (score < bestScore) {
                                    bestScore = score;
                                    bestMove = move;
                                }
                                if(score < beta) beta = score;
                                if(beta <= alpha){
                                    bestMove = move;
                                    break outerloop;
                                }
                            }
//                            if(turn) {
//                                if (nextScore > score) {
//                                    score = nextScore;
////                                        bestMove = nextMove;
//                                }
//                                if(score > alpha) alpha = score;
//                                if(beta <= alpha){
//                                    return score;
//                                }
//                            }
//                            else {
//                                if (nextScore < score) {
//                                    score = nextScore;
////                                        bestMove = nextMove;
//                                }
//                                if(score < beta) beta = score;
//                                if(beta <= alpha){
//                                    return score;
//                                }
//                            }
                        }
                    }
                }
            }
        }
//        System.out.println("Best score: "+bestScore);
//        System.out.println("Best move: "+Arrays.toString(bestMove));
//        System.out.println();
        // Make the best move
        if (bestMove != null) {
            Piece removedPiece = sessionVars.getBoardObject().getPieceAt(bestMove[2], bestMove[3]);
            if(removedPiece != null) {
                sessionVars.removePiece(removedPiece.toString(), removedPiece.isWhite());
            }
            sessionVars.getBoardObject().movePiece(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
            sessionVars.getBoardObject().setPiece(bestMove[0], bestMove[1], null);
        }
    }

//    public void makeRandomMove(boolean isWhite, String sessionId){
//        SessionVariables sessionVars = getSessionVariables(sessionId);
//        Piece[][] boardArray = sessionVars.getBoardObject().getBoardArray();
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++){
//                if (boardArray[i][j] == null || boardArray[i][j].isWhite() != isWhite) continue;
//                boolean[][] tempMoves = findSelections(sessionId, i, j);
//                for(int x=0; x<8; x++){
//                    for(int y=0; y<8; y++){
//                        Piece removedPiece = sessionVars.getBoardObject().getPieceAt(x, y);
//                        if(removedPiece != null) {
//                            sessionVars.removePiece(removedPiece.toString(), removedPiece.isWhite());
//                        }
//                        sessionVars.getBoardObject().movePiece(i, j, x, y);
//                        sessionVars.getBoardObject().setPiece(i, j, null);
//                    }
//               }
//            }
//        }
//    }

    public void makeComputerMove2(boolean isWhite, String sessionId){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        int foresightValue = 2 + (12/(sessionVars.blackPiecesLeft()+sessionVars.whitePiecesLeft()));
//        System.out.println("Depth: "+foresightValue);
//        System.out.println("calculation: "+(12.0/(sessionVars.blackPiecesLeft()+sessionVars.whitePiecesLeft())));
        Piece[][] pieceBoard = sessionVars.getBoardObject().getBoardArray();
        ArrayList<int[]> validMoves = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        // Collect all valid moves and evaluate them
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieceBoard[i][j] == null || pieceBoard[i][j].isWhite() != isWhite) continue;
                boolean[][] tempMoves = findSelections(sessionId, i, j);
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (tempMoves[x][y]) {
                            int[] move = new int[]{i, j, x, y};
                            validMoves.add(move);

                            // Evaluate the move
                            int score = evaluateMove(pieceBoard, move, foresightValue, Integer.MIN_VALUE, Integer.MAX_VALUE, sessionVars.getMovesMade());
//                            int score = evaluateMove(pieceBoard, move, 2);
                            // System.out.println(Arrays.toString(move) + ": " + score);
                            if (score > bestScore) {
                                bestScore = score;
                                bestMove = move;
                            }
                        }
                    }
                }
            }
        }
        // Make the best move
        if (bestMove != null) {
            Piece removedPiece = sessionVars.getBoardObject().getPieceAt(bestMove[2], bestMove[3]);
            if(removedPiece != null) {
                sessionVars.removePiece(removedPiece.toString(), removedPiece.isWhite());
            }
            sessionVars.getBoardObject().movePiece(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
            sessionVars.getBoardObject().setPiece(bestMove[0], bestMove[1], null);
        }
    }

    public int evaluateMove(Piece[][] evaluationBoard, int[] move, int foresight, int alpha, int beta, int movesMade) {
        // make a copy of board
        Piece[][] tempBoard = copyPieceBoard(evaluationBoard);
//        Piece[][] tempBoard = copyPieceBoard(pieceBoard);
        // make the move on the copy
//        tempBoard[move[0]][move[1]] = null;
//        tempBoard[move[2]][move[3]] = evaluationBoard[move[0]][move[1]];
        movePieceCopy(move[0], move[1], move[2], move[3], tempBoard);
        setPieceCopy(move[0], move[1], null, tempBoard);
        movesMade++;
        // find opposing king color
//        boolean kingColor = !tempBoard[move[2]][move[3]].isWhite();
        // check if opposing king is in check
//        if(isInCheck(kingColor, tempBoard)){
//            score += 40;
//        }
//        if(isCheckMate(sessionId, kingColor)){
//            if(isInCheck(kingColor, pieceBoard)){
//                score += Integer.MAX_VALUE;
//            }
//            else{
//                score -= 50;
//            }
//        }
//        if(foresight == 2) {
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    if (tempBoard[i][j] != null) {
//                        System.out.print(tempBoard[i][j].toString().charAt(0));
//                    } else {
//                        System.out.print("_");
//                    }
//                }
//                System.out.println();
//            }
//        }
//        if(foresight == 2) {
//            if (checkMate) System.out.println("CHECKMATE FOUND !!!!!!!!!!!!!!!!!!!");
//        }
        if(tempBoard[move[2]][move[3]].isWhite()) { // white turn
            boolean blackInCheckmate = isCheckMate(tempBoard, false);
            if (isInCheck(false, tempBoard)) {
                if(blackInCheckmate){
//                    System.out.println("returning early");
                    return Integer.MAX_VALUE - 1000 + (10 * foresight);
                }
            }
        }
        else{
            boolean whiteInCheckmate = isCheckMate(tempBoard, true);
            if(isInCheck(true, tempBoard)){
                if(whiteInCheckmate){
//                    System.out.println("returning early");
                    return Integer.MIN_VALUE + 1000 - (10 * foresight);
                }
            }
        }
        // add foresight into the next move, avoiding ones where you will lose score
        if(foresight > 0){
            // run a loop and run evaluate move on every
//            System.out.println("Moving from "+move[0]+","+move[1]+" to "+move[2]+","+move[3]);
            return foresightScore(tempBoard, foresight, !tempBoard[move[2]][move[3]].isWhite(), alpha, beta, movesMade);
        }

        return evaluatePosition(tempBoard, movesMade, tempBoard[move[2]][move[3]].isWhite());
    }

    public int foresightScore(Piece[][] pieceBoard, int foresight, boolean turn, int alpha, int beta, int movesMade){
//        System.out.println("side is "+turn);
        int score = 0;
//        int[] bestMove = null;
        boolean setMove = false;
        // Find the highest score possible for the next move
        for(int i=0; i<8; i++){ // loop through pieces they could choose
            for(int j=0; j<8; j++){
                if(pieceBoard[i][j] != null && (pieceBoard[i][j].isWhite() == turn)){
                    boolean[][] tempMoves = findSelectionsFromBoard(i, j, pieceBoard);
                    for(int x=0; x<8; x++){ // loop through the moves of the piece at i,j
                        for(int y=0; y<8; y++){
                            if(tempMoves[x][y]){ // if a valid move
                                int[] nextMove = new int[]{i, j, x, y};
                                int nextScore = evaluateMove(pieceBoard, nextMove, foresight-1, alpha, beta, movesMade);
                                if(!setMove){
//                                    bestMove = nextMove;
                                    score = nextScore;
                                    setMove = true;
                                }
                                if(turn) {
                                    if (nextScore > score) {
                                        score = nextScore;
//                                        bestMove = nextMove;
                                    }
                                    if(score > alpha) alpha = score;
                                    if(beta <= alpha){
                                        return score;
                                    }
                                }
                                else {
                                    if (nextScore < score) {
                                        score = nextScore;
//                                        bestMove = nextMove;
                                    }
                                    if(score < beta) beta = score;
                                    if(beta <= alpha){
                                        return score;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        System.out.println("best move for white is "+bestMove[0]+","+bestMove[1]+" to "+bestMove[2]+","+bestMove[3]);
        return score;
    }

    public int addSquareScore(int row, int col){
        int[][] squareScores = new int[][]{
                {0, 1, 2, 3, 3, 2, 1, 0},
                {1, 2, 3, 4, 4, 3, 2, 1},
                {2, 3, 4, 5, 5, 4, 3, 2},
                {3, 4, 5, 6, 6, 5, 4, 3},
                {3, 4, 5, 6, 6, 5, 4, 3},
                {2, 3, 4, 5, 5, 4, 3, 2},
                {1, 2, 3, 4, 4, 3, 2, 1},
                {0, 1, 2, 3, 3, 2, 1, 0}
        };
        return squareScores[row][col];
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

