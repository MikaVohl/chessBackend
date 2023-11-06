package com.example.chessBackend.game;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariables {
    private static final Map<String, SessionVariables> sessionVariablesMap = new HashMap<>();

    public static void addSessionId(String sessionId){
        sessionVariablesMap.put(sessionId, new SessionVariables(sessionId));
    }

    public static void getSessionVariables(String sessionId){
        sessionVariablesMap.get(sessionId);
    }
}
