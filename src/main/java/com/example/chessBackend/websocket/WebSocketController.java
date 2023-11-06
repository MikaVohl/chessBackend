package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.UUID;

@Controller
public class WebSocketController {
//    Processor gameProcessor = new Processor();


    @SubscribeMapping("/serverCommands")
    public String onSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        return sessionId;
    }

    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
    @SendTo("/topic/serverCommands")   // This is the topic to which the processed messages will be sent
    public String handleMessage(Message<String> clientMessage) {
        MessageHeaders msgHeaders = clientMessage.getHeaders();
        LinkedMultiValueMap<?,?> headers = (LinkedMultiValueMap<?,?>) msgHeaders.get("nativeHeaders");
        String clientId = null;
        if(headers != null)
            clientId = (String) headers.get("id").get(0);
        Processor.addSessionId(clientId);
        String message = clientMessage.getPayload();
        System.out.println("received a message from client "+clientId);

        int row = message.charAt(0)-48;
        int col = message.charAt(1)-48;
        if(Processor.isFirstClick(clientId)){
            return sendSelections(clientId, row, col);
        }
        return null;
    }

//    public NewGameResponse createGame(SimpMessageHeaderAccessor headerAccessor) throws GameFullException {
//        String sessionId = headerAccessor.getSessionId();
//        logger.info("Subscribe frame recieved at \"app/game/create\" from: {}", sessionId);
//        UUID gameId = gameService.createGame(sessionId);
//        registry.setGameId(sessionId, gameId);
//
//        return new NewGameResponse(gameId, Player.BLACK);
//    }

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
    public String sendSelections(String sessionId, int row, int col){
        return Processor.findSelections(sessionId, row, col);
    }


//    @MessageMapping("/connect")  // This is the endpoint where clients send their messages
//    public void onConnect() {
//        System.out.println("CONNECTED");
//
//    }


}
