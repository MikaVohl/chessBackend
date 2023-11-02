package com.example.chessBackend.websocket;

public class ReceiveMoves {
    public static String processMoves(String message){
        if(message.length() < 2) return "Unknown message received: "+message;
        return "Tile at "+(char)(message.charAt(1) +17)+(8-message.charAt(0)+48);
    }
}
