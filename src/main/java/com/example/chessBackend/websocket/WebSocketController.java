package com.example.chessBackend.websocket;

import com.example.chessBackend.game.Processor;
import com.example.chessBackend.game.SessionVariables;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;

@Controller
public class WebSocketController {
//    SimpMessagingTemplate simpMessagingTemplate = new SimpMessagingTemplate(new MessageChannel() {
//        @Override
//        public boolean send(Message<?> message, long timeout) {
//            return false;
//        }
//    });


    @SubscribeMapping("/serverCommands")
    public String onSubscribe(SimpMessageHeaderAccessor headerAccessor) {
//        notifySecondConnection();
//        simpMessagingTemplate.convertAndSend("/topic/test", "testing");
        String joinCode = Processor.generateCode();
        String sessionId = headerAccessor.getSessionId();
        return joinCode+sessionId+Processor.getSessionVariables(sessionId).getBoardObject().decodeBoardIntoImg();
    }


//    @MessageMapping("/fleet/{fleetId}/driver/{driverId}")
//    public void simple(@DestinationVariable String fleetId, @DestinationVariable String driverId) {
//        simpMessagingTemplate.convertAndSend("/topic/fleet/" + fleetId, new Simple(fleetId, driverId));
//    }

    @MessageMapping("/connection")  // This is the endpoint where clients send their messages
    public void connectionNegotation(Message<String> clientMessage) {
        MessageHeaders msgHeaders = clientMessage.getHeaders();
        LinkedMultiValueMap<?,?> headers = (LinkedMultiValueMap<?,?>) msgHeaders.get("nativeHeaders");
        String clientId = null;
        if(headers != null)
            clientId = (String) headers.get("id").get(0);
        String message = clientMessage.getPayload();

        if(message.equals("cpu")){
            System.out.println("CPU GAME");
            Processor.getSessionVariables(clientId).setCpuGame(true);
        }
    }

    @MessageMapping("/incomingInfo")  // This is the endpoint where clients send their messages
    @SendToUser("/topic/serverCommands")   // This is the topic to which the processed messages will be sent
    public String handleMessage(Message<String> clientMessage) {
        MessageHeaders msgHeaders = clientMessage.getHeaders();
        LinkedMultiValueMap<?,?> headers = (LinkedMultiValueMap<?,?>) msgHeaders.get("nativeHeaders");
        String clientId = null;
        if(headers != null)
            clientId = (String) headers.get("id").get(0);
        String message = clientMessage.getPayload();

        int row = message.charAt(0)-48;
        int col = message.charAt(1)-48;

        return Processor.processClick(clientId, row, col);
    }

}
