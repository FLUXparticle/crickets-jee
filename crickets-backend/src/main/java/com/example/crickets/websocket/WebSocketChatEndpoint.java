package com.example.crickets.websocket;

import com.example.crickets.service.*;
import jakarta.enterprise.context.*;
import jakarta.enterprise.event.*;
import jakarta.inject.*;
import jakarta.json.bind.*;
import jakarta.websocket.*;
import jakarta.websocket.server.*;
import org.jboss.logging.*;

import java.security.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
@ServerEndpoint("/ws/chat")
public class WebSocketChatEndpoint {

    private static final Logger LOGGER = Logger.getLogger(WebSocketChatEndpoint.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    @Inject
    private ChatService chatService;

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.infof("Client connected: %s", session.getId());
        SESSIONS.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.infof("Message received from %s: %s", session.getId(), message);
        ChatMessage chatMessage = fromJson(message);

        // Broadcast vorbereiten
        Principal principal = session.getUserPrincipal();
        chatMessage.setCreatorName(principal.getName());
        chatMessage.setCreatedAt(LocalTime.now().format(FORMATTER)); // Zeit hinzufügen


        // Senden an alle
        sendToAll(chatMessage);

        // Weiterleiten der Nachricht an andere Server
        chatService.broadcastMessage(chatMessage);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.infof("Client disconnected: %s Reason: %s", session.getId(), reason);
        SESSIONS.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.errorf(throwable, "Error on session %s", session.getId());
    }

    public void sendToAll(@Observes ChatMessage message) {
        String json = toJson(message);
        for (Session session : SESSIONS.values()) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(json);
            }
        }
    }

    private static ChatMessage fromJson(String message) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            // JSON -> Java-Objekt
            return jsonb.fromJson(message, ChatMessage.class);
        } catch (Exception e) {
            LOGGER.error(e);
            return new ChatMessage(e.toString());
        }
    }

    private static String toJson(ChatMessage message) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            // Java-Objekt -> JSON
            return jsonb.toJson(message);
        } catch (Exception e) {
            LOGGER.error(e);
            return "{\"content\":\"" + e + "\"}";
        }
    }

}
