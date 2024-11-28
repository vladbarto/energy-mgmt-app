package ro.tucn.energy_mgmt_monitoring_comm.service.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.io.IOException;

@Slf4j
@Service
public class WebSocketService {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Adds a session for a specific client or device.
     */
    public synchronized void addSession(String key, WebSocketSession session) {
        sessionMap.put(key, session);
        log.info("WebSocket session added for key: {}", key);
    }

    /**
     * Removes a session for a specific key.
     */
    public synchronized void removeSession(String key) {
        sessionMap.remove(key);
        log.info("WebSocket session removed for key: {}", key);
    }

    /**
     * Sends a message to a specific key's session.
     */
    public synchronized void sendMessageToKey(String key, String message) {
        WebSocketSession session = sessionMap.get(key);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                log.info("Message sent to key {}: {}", key, message);
            } catch (IOException e) {
                log.error("Failed to send message to key {}: {}", key, e.getMessage());
            }
        } else {
            log.warn("No active WebSocket session for key: {}", key);
        }
    }

    /**
     * Sends a message to all sessions (if needed for debugging or testing).
     */
    public synchronized void sendMessageToAll(String message) {
        sessionMap.forEach((key, session) -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("Failed to send message to key {}: {}", key, e.getMessage());
                }
            }
        });
    }

    // Checks if a session exists for a specific user
    public synchronized boolean hasSession(String userId) {
        WebSocketSession session = sessionMap.get(userId);
        return session != null && session.isOpen();
    }
}
