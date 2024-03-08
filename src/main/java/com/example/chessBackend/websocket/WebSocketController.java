package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    Processor processor = new Processor();

    @SubscribeMapping("/serverCommands")
    public String onSubscribe(SimpMessageHeaderAccessor headerAccessor) {
        // String joinCode = processor.generateCode(); // this joincode is no longer necessary, must remove once messaging system is better
        String sessionId = headerAccessor.getSessionId();
        return sessionId+processor.getSessionVariables(sessionId).getBoardObject().decodeBoardIntoImg();
    }

    @MessageMapping("/incomingInfo") // This is the endpoint where clients send their messages
    @SendToUser("/topic/serverCommands")
    public String handleMessage(Message<String> clientMessage) {
        MessageHeaders msgHeaders = clientMessage.getHeaders();
        LinkedMultiValueMap<?,?> headers = (LinkedMultiValueMap<?,?>) msgHeaders.get("nativeHeaders");
        String clientId = null;
        if(headers != null)
            clientId = (String) headers.get("id").get(0);
        String message = clientMessage.getPayload();
        if(message.equals("1")){
            return processor.computerTurn(clientId);

        }

        int row = message.charAt(0)-48;
        int col = message.charAt(1)-48;

        return processor.processClick(clientId, row, col);
    }

}
