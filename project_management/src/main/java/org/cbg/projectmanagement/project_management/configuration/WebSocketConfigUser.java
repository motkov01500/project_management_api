package org.cbg.projectmanagement.project_management.configuration;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocket")
public class WebSocketConfigUser {

    private static Set<Session> userSessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        userSessions.add(session);
    }

    @OnMessage
    public void onMessage(String message) {
        for (Session session : userSessions) {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        userSessions.remove(session);
    }
}
