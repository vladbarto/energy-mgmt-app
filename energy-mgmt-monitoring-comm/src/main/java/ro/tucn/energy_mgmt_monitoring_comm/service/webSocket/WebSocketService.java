package ro.tucn.energy_mgmt_monitoring_comm.service.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Service
public class WebSocketService {

    private WebSocketSession session;

    /**
     * Adds a global session.
     */
    public synchronized void addSession(WebSocketSession newSession) {
        if (session != null && session.isOpen()) {
            try {
                log.warn("Closing existing WebSocket session.");
                session.close();
            } catch (IOException e) {
                log.error("Failed to close existing session: {}", e.getMessage());
            }
        }
        session = newSession;
        log.info("New WebSocket session established.");
    }

    /**
     * Removes the global session.
     */
    public synchronized void removeSession() {
        session = null;
        log.info("WebSocket session removed.");
    }

    /**
     * Sends a message to the WebSocket session if it is open.
     */
    public synchronized void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                log.info("Message sent: {}", message);
            } catch (IOException e) {
                log.error("Failed to send message: {}", e.getMessage());
            }
        } else {
            log.warn("No open WebSocket session to send message.");
        }
    }

    /**
     * Checks if there is an active session.
     */
    public synchronized boolean hasSession() {
        return session != null && session.isOpen();
    }
}
