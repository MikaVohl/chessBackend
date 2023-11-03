package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;

public class ReceiveMoves {
    public static int messageCount = 0;
    public static String processMoves(String message){
        if(message.length() < 2) return "Unknown message received: "+message;
        messageCount++;
        return "Message #"+messageCount+". Tile at "+(char)(message.charAt(1) +17)+(8-message.charAt(0)+48)+". The piece at this tile is "+ Processor.fetchPiece(message.charAt(0)-48, message.charAt(1)-48);
    }
}
