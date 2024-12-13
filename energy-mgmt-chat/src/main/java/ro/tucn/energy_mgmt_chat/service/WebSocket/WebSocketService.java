package ro.tucn.energy_mgmt_chat.service.WebSocket;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {
    /**
     * Adds a session for a specific client or device.
     */
    void addSession(String key, WebSocketSession session);

    /**
     * Removes a session for a specific key.
     */
    void removeSession(String key);

    /**
     * Sends a message to a specific key's session.
     */
    void sendMessageToKey(String key, String message);

    /**
     * Sends a message to all sessions (if needed for debugging or testing).
     */
    void sendMessageToAll(String message);

    /**
     * Checks if a session exists for a specific user
     */
    boolean hasSession(String userId);
}
