package org.cbg.projectmanagement.project_management.configuration;

import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocket")
public class WebSocketConfigUser {

    private static HashSet<Session> userSessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        userSessions.add(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        for (Session userSession : userSessions) {
            try {
                userSession.getBasicRemote().sendObject(message);
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
