package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
//    Processor gameProcessor = new Processor();


    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
    @SendTo("/topic/possibleMoves")   // This is the topic to which the processed messages will be sent
    public String handleMessage(String message) {
        int row = message.charAt(0)-48;
        int col = message.charAt(1)-48;
//        if(Processor.isFirstClick()){
            return sendSelections(row, col);
//        }
    }

//    //    @SendTo("/topic/receivedInstruction")   // This is the topic to which the processed messages will be sent
//    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
//    @SendTo("/topic/possibleMoves")
//    public String handleMessage(String message) {
//        int row = message.charAt(0)-48;
//        int col = message.charAt(0)-48;
//        return "test";
//        // Handle the received message here and return a response (if needed)
////        return ReceiveMoves.processMoves(message);
////        if(Processor.isFirstClick()){
////            return sendSelections(row, col);
////        }
//    }
//
//    @SendTo("/topic/possibleMoves")
    public String sendSelections(int row, int col){
        return Processor.findSelections(row, col);
    }


    @MessageMapping("/connect")  // This is the endpoint where clients send their messages
    public void onConnect() {
        System.out.println("CONNECTED");

    }


}
