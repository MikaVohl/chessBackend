package com.example.chessBackend.game;

import com.example.chessBackend.game.pieces.Piece;

import java.util.*;


public class Processor {

    private final Map<String, SessionVariables> sessionVariablesMap = new HashMap<>();

    public void addSessionId(String sessionId){
        sessionVariablesMap.put(sessionId, new SessionVariables(sessionId));
    }

    public SessionVariables getSessionVariables(String sessionId){
        if(!sessionVariablesMap.containsKey(sessionId)){
            System.out.println("Session ID not found, adding new session ID: "+sessionId);
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
            sessionVars.getBoardObject().movePiece(firstR, firstC, row, col);
            sessionVars.getBoardObject().setPiece(firstR, firstC, null);

            output = ("2"+sessionVars.getBoardObject().decodeBoardIntoImg());

            sessionVars.setWhiteTurn(!sessionVars.isWhiteTurn());

            if(isCheckMate(sessionId, sessionVars.isWhiteTurn())){
                if(!kingInCheck(sessionId, sessionVars.isWhiteTurn())){ // checkmate case
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

    public String processClick(String sessionId, int row, int col){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        String output = "";

        if(sessionVars.getFirstClick())
            output += firstClick(sessionId, row, col);
        else
            output+= secondClick(sessionId, row, col);

        return output;
    }

    public boolean isCheckMate(String sessionId, boolean isCheckingWhite){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pieceHasMoves(sessionId, i, j)) return false;
            }
        }
        return true;
    }

    public boolean kingInCheck(String sessionId, boolean isCheckingWhite){
        SessionVariables sessionVars = getSessionVariables(sessionId);
        Board board = sessionVars.getBoardObject();
        int[] kingCoords = kingLocation(isCheckingWhite, board.getBoardArray());
        return findSelectionsNoChecks(board.getBoardArray(), !isCheckingWhite, kingCoords[0], kingCoords[1])[kingCoords[0]][kingCoords[1]];
    }

    public boolean pieceHasMoves(String sessionId, int row, int col){
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
        boolean[][] pieceMoves = selectedPiece.generateMoves(pieceBoard);
        revealedChecks(selectedPiece.isWhite(), pieceMoves, pieceBoard, row, col);
        return pieceMoves;
    }

    // Does not sift out moves that would put the king in check
    public boolean[][] findSelectionsNoChecks(Piece[][] pieceBoard, boolean isWhite, int row, int col){
        Piece selectedPiece = pieceBoard[row][col];
        if(selectedPiece == null || selectedPiece.isWhite() != isWhite) return new boolean[8][8];

        boolean[][] pieceMoves = selectedPiece.generateMoves(pieceBoard);

        return pieceMoves;
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

    public void revealedChecks(boolean whiteKing, boolean[][] currentSelections, Piece[][] boardCopy, int startRow, int startCol){
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
            System.arraycopy(referenceBoard[i], 0, outputBoard[i], 0, 8);
        }
        return outputBoard;
    }

    public void setPieceCopy(int row, int col, Piece piece, Piece[][] chessBoardCopy){
        chessBoardCopy[row][col] = piece;
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

    public void makeComputerMove(boolean isWhite, String sessionId){
        SessionVariables sessionVars = getSessionVariables(sessionId);
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
                            int score = evaluateMove(pieceBoard, move, 2);
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
            sessionVars.getBoardObject().movePiece(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
            sessionVars.getBoardObject().setPiece(bestMove[0], bestMove[1], null);
        }
    }

    public int evaluateMove(Piece[][] evaluationBoard, int[] move, int foresight) {
//        SessionVariables sessionVars = getSessionVariables(sessionId);
//        Piece[][] pieceBoard = sessionVars.getBoardObject().getBoardArray();
        Piece[][] pieceBoard = evaluationBoard;
        int score = 0;
    
        // If the move captures an opponent's piece, add the value of the piece to the score
        Piece capturedPiece = pieceBoard[move[2]][move[3]];
        if (capturedPiece != null) {
            score += 20 * capturedPiece.getValue();
        }
        // encourage moving pawns
        score += 1 * (pieceBoard[move[0]][move[1]].toString().equals("Pawn") ? 1 : 0);
        // discourage moving the king
        score -= 1 * (pieceBoard[move[0]][move[1]].toString().equals("King") ? 1 : 0);

        score -= addSquareScore(move[0], move[1]); // discourage moving away from favourable positions
        score += addSquareScore(move[2], move[3]); // encourage moving towards favourable positions
    
        // make a copy of board
        Piece[][] tempBoard = copyPieceBoard(pieceBoard);
        // make the move on the copy
        tempBoard[move[0]][move[1]] = null;
        tempBoard[move[2]][move[3]] = pieceBoard[move[0]][move[1]];
        // find opposing king color
        boolean kingColor = !tempBoard[move[2]][move[3]].isWhite();
        // check if opposing king is in check
        if(isInCheck(kingColor, tempBoard)){
            score += 40;
        }
//        if(isCheckMate(sessionId, kingColor)){
//            if(isInCheck(kingColor, pieceBoard)){
//                score += Integer.MAX_VALUE;
//            }
//            else{
//                score -= 50;
//            }
//        }

        // add foresight into the next move, avoiding ones where you will lose score
        if(foresight > 0){
//            System.out.println("Moving from "+move[0]+","+move[1]+" to "+move[2]+","+move[3]);
            score -= foresightScore(tempBoard, foresight, !tempBoard[move[2]][move[3]].isWhite());
        }

        return score;
    }

    public int foresightScore(Piece[][] pieceBoard, int foresight, boolean turn){
        int score = Integer.MIN_VALUE;
        int[] bestMove = null;
        // Simulate making the computer move
//        Piece capturedPiece = pieceBoard[move[2]][move[3]];
//        Piece movedPiece = pieceBoard[move[0]][move[1]];
//        pieceBoard[move[0]][move[1]] = null;
//        pieceBoard[move[2]][move[3]] = movedPiece;

        // Find the highest score possible for the opponent's next move
        for(int i=0; i<8; i++){ // loop through opponent pieces they could choose
            for(int j=0; j<8; j++){
                if(pieceBoard[i][j] != null && (pieceBoard[i][j].isWhite() == turn)){
                    boolean[][] tempMoves = findSelectionsFromBoard(i, j, pieceBoard);
//                    System.out.println("White moves "+i+","+j);
//                    System.out.println(Arrays.deepToString(tempMoves).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
                    for(int x=0; x<8; x++){ // loop through the moves of the piece at i,j
                        for(int y=0; y<8; y++){
                            if(tempMoves[x][y]){ // if a valid move
                                int[] nextMove = new int[]{i, j, x, y};
                                int nextScore = evaluateMove(pieceBoard, nextMove, foresight-1);
                                if(nextScore > score){
                                    score = nextScore;
                                    bestMove = nextMove;
                                }
                            }
                        }
                    }
//                    System.out.println("White score is "+score);
                }
//                System.out.println("EVALUATE MOVE RESULT: "+evaluateMove(pieceBoard, new int[]{7,5,2,0}, 0));
            }
        }

//        // Undo the move
//        pieceBoard[move[0]][move[1]] = movedPiece;
//        pieceBoard[move[2]][move[3]] = capturedPiece;

        // System.out.println("Best move is "+(bestMove == null ? "null" : (Arrays.toString(bestMove) + ": " + score)));
//        System.out.println("best move is "+bestMove[0]+","+bestMove[1]+" to "+bestMove[2]+","+bestMove[3]);
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

