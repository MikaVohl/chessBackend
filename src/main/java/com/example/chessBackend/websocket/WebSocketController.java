package com.example.chessBackend.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

//    @MessageMapping({"/topic", "/"})  // This is the endpoint where clients send their messages
    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
    @SendTo("/topic/receivedInstruction")   // This is the topic to which the processed messages will be sent
    public String handleMessage(String message) {
        // Handle the received message here and return a response (if needed)
        return ReceiveMoves.processMoves(message);
    }

}
