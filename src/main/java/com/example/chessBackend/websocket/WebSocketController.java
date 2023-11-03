package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    Processor gameProcessor = new Processor();

    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
    @SendTo("/topic/receivedInstruction")   // This is the topic to which the processed messages will be sent
    public String handleMessage(String message) {
        // Handle the received message here and return a response (if needed)
        return ReceiveMoves.processMoves(message);
    }

    @MessageMapping("/connect")  // This is the endpoint where clients send their messages
    public void onConnect() {
        System.out.println("CONNECTED");

    }


}
