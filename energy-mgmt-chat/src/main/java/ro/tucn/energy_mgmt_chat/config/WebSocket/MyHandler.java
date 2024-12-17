package ro.tucn.energy_mgmt_chat.config.WebSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ro.tucn.energy_mgmt_chat.dto.message.MessageRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageResponseDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageStatus;
import ro.tucn.energy_mgmt_chat.dto.wsAction.ActionType;
import ro.tucn.energy_mgmt_chat.dto.wsAction.WsActionRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.wsAction.WsActionResponseDTO;
import ro.tucn.energy_mgmt_chat.security.filter.AuthorizationFilter;
import ro.tucn.energy_mgmt_chat.service.WebSocket.WebSocketService;
import ro.tucn.energy_mgmt_chat.service.message.MessageService;
import ro.tucn.energy_mgmt_chat.security.util.JwtUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ro.tucn.energy_mgmt_chat.config.WebSocket.WsUtils.extractQueryParam;


@Slf4j
@RequiredArgsConstructor
public class MyHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;
    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            // Extract the JWT token from the WebSocket URI query parameters
            String query = session.getUri().getQuery();
            String token = extractQueryParam(query, "token");

            if (token == null || token.isEmpty()) {
                log.warn("Missing JWT token in WebSocket connection.");
                session.close(CloseStatus.BAD_DATA.withReason("Missing JWT token"));
                return;
            }

            // Validate and parse the JWT token
            if (!AuthorizationFilter.validateToken(token)) {
                log.warn("Invalid JWT token in WebSocket connection.");
                session.close(CloseStatus.BAD_DATA.withReason("Invalid JWT token"));
                return;
            }

            // Extract userId from the validated JWT token
            String username = JwtUtils.extractUsernameFromToken(token);

            if (username == null || username.isEmpty()) {
                log.warn("JWT token does not contain a valid userId.");
                session.close(CloseStatus.BAD_DATA.withReason("Invalid userId in JWT token"));
                return;
            }

            // Register this WebSocket session with the userId in the WebSocketService
            webSocketService.addSession(username, session);
            log.info("WebSocket connection established for userId: {}", username);

        } catch (Exception e) {
            log.error("Failed to establish WebSocket connection: {}", e.getMessage());
            try {
                session.close(CloseStatus.SERVER_ERROR.withReason("Failed to process JWT token"));
            } catch (IOException ioException) {
                log.error("Failed to close WebSocket session: {}", ioException.getMessage());
            }
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        objectMapper.registerModule(new JavaTimeModule());
        log.info("Received message: {}", message.getPayload());
        try {
            WsActionRequestDTO wsActionRequestDTO = objectMapper.readValue(message.getPayload(), WsActionRequestDTO.class);

            if(ActionType.SEND_MESSAGE == wsActionRequestDTO.getType()) {
                log.info("Chat message received: {}", wsActionRequestDTO);
                handleSendMessageType(wsActionRequestDTO);
            } else if (ActionType.TYPING == wsActionRequestDTO.getType() ||
                    ActionType.STOP_TYPING == wsActionRequestDTO.getType()) {
                log.info("Typing info received: {}", wsActionRequestDTO);
                handleTypingType(wsActionRequestDTO);
            } else if (ActionType.FETCH_MESSAGES == wsActionRequestDTO.getType()) {
                log.info("User {} is fetching messages from user {}", wsActionRequestDTO.getTransmitter(), wsActionRequestDTO.getReceiver());
                handleFetchMessagesType(wsActionRequestDTO);
            } else if (ActionType.RECEIVED == wsActionRequestDTO.getType()) {
                log.info("User {} has connected and received the messages", wsActionRequestDTO.getTransmitter());
                handleReceivedMessagesType(wsActionRequestDTO);
            } else if (ActionType.SEEN_SIGNAL == wsActionRequestDTO.getType()) {
                log.info("HEY! SEEN SIGNAL RECEIVED from {}", wsActionRequestDTO.getTransmitter());
                handleSeenMessagesType(wsActionRequestDTO);
                handleSeenSignalType(wsActionRequestDTO);
            }

        } catch (IOException e) {
            log.error("Failed to parse incoming WebSocket message: {}", message.getPayload(), e);
        } catch (Exception e) {
            log.error("Unexpected error while handling WebSocket message: {}", e.getMessage(), e);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Handle connection closure
        try {
            String query = session.getUri().getQuery();
            String token = extractQueryParam(query, "token");

            // Extract userId from the validated JWT token
            String username = JwtUtils.extractUsernameFromToken(token);

            if (!username.isEmpty()) {
                webSocketService.removeSession(username);
                log.info("WebSocket connection closed for user with username: {}", username);
                log.info("Connection closed for session ID: {}, CloseStatus: {}", session.getId(), status);
            }

        } catch (Exception e) {
            log.error("Failed to process WebSocket disconnection: {}", e.getMessage());
        }
    }

    /**
     * HANDLERS
     */

    private void handleSendMessageType(WsActionRequestDTO requestDTO) {
        MessageRequestDTO messageRequestDTO = MessageRequestDTO.builder()
                .transmitter(requestDTO.getTransmitter())
                .receiver(requestDTO.getReceiver())
                .text(requestDTO.getText())
                .sendingTime(LocalDateTime.now()) // Generate the timestamp
                .status(MessageStatus.SENT)
                .build();

        log.info("Message DTO with timestamp filled in: {}", messageRequestDTO.toString());

        MessageResponseDTO messageResponseDTO = messageService.save(messageRequestDTO);
        log.info("Message response: {}", messageResponseDTO.toString());

        try {
            WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
                    .transmitter(requestDTO.getTransmitter())
                    .receiver(requestDTO.getReceiver())
                    .text(requestDTO.getText())
                    .type(ActionType.RECEIVE_MESSAGE)
                    .status(MessageStatus.SENT)
                    .messageId(messageResponseDTO.getId())
                    .build();

            if(messageResponseDTO.getReceiver().equals("announcements")) {
                log.info("Sending announcement to all");
                webSocketService.sendMessageToAll(objectMapper.writeValueAsString(wsActionResponseDTO));
            } else if(webSocketService.hasSession(requestDTO.getReceiver())) {
                log.info("Sending out typing notification to connected receiver: {}", wsActionResponseDTO);
                webSocketService.sendMessageToKey(
                        requestDTO.getReceiver(),
                        objectMapper.writeValueAsString(wsActionResponseDTO));
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleTypingType(WsActionRequestDTO requestDTO) {
        WsActionResponseDTO responseDTO = WsActionResponseDTO.builder()
                .type(requestDTO.getType())
                .transmitter(requestDTO.getTransmitter())
                .receiver(requestDTO.getReceiver())
                .build();

        try {
            if(webSocketService.hasSession(responseDTO.getReceiver())) {
                log.info("Sending out typing notification to connected receiver: {}", responseDTO);
                webSocketService.sendMessageToKey(responseDTO.getReceiver(), objectMapper.writeValueAsString(responseDTO));
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleFetchMessagesType(WsActionRequestDTO requestDTO) {
        String whoMakesTheRequest = requestDTO.getTransmitter();
        String whoHeHasAConversationWith = requestDTO.getReceiver();

        List<MessageResponseDTO> messageResponseDTOList;
        if(whoHeHasAConversationWith.equals("announcements")) {
            // nume hardcodat, user dummy creat
            messageResponseDTOList = messageService.getAnnouncements(
                    whoHeHasAConversationWith
            );
        } else {
            messageResponseDTOList = messageService.getMessagesBetweenUsers(
                    whoMakesTheRequest,
                    whoHeHasAConversationWith);
        }

        try {
            log.info("Fetching messages from receiver: {}, requested by {}", requestDTO.getReceiver(), whoMakesTheRequest);

            for (MessageResponseDTO messageResponseDTO : messageResponseDTOList) {
                log.info("Ia sa vedem mesajul de pe grup: {}", messageResponseDTO);

                WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
                        .type(ActionType.FETCH_MESSAGES)
                        .transmitter(messageResponseDTO.getTransmitter())
                        .receiver(messageResponseDTO.getReceiver()) // sau whoMakesTheRequest mai corect, dar irelevant aici, relevant in if
                        .status(messageResponseDTO.getStatus())
                        .text(messageResponseDTO.getText())
                        .messageId(messageResponseDTO.getId())
                        .sendingTime(messageResponseDTO.getSendingTime())
                        .build();

                if(webSocketService.hasSession(whoMakesTheRequest)) {
                    log.info("Sending message to who solicited it: {}", wsActionResponseDTO);
                    webSocketService.sendMessageToKey(whoMakesTheRequest, objectMapper.writeValueAsString(wsActionResponseDTO));
                }
            }

            // Dupa ce userul isi aduce (fetch toate mesajele), celalalt user primeste de la server un SEEN_SIGNAL
            WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
                    .type(ActionType.SEEN_SIGNAL)
                    .transmitter(whoMakesTheRequest)
                    .receiver(whoHeHasAConversationWith)
                    .build();

            if(webSocketService.hasSession(whoHeHasAConversationWith)) {
                log.info("Sending Seen Signal to chat partner: {}", wsActionResponseDTO);
                webSocketService.sendMessageToKey(whoHeHasAConversationWith, objectMapper.writeValueAsString(wsActionResponseDTO));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleReceivedMessagesType(WsActionRequestDTO requestDTO) {
        try {
            int affectedRows = messageService.updateMessageStatusesToReceived(requestDTO.getTransmitter());
            log.info("Affected rows by updating to RECEIVED request: {}", affectedRows);
        } catch (Exception e) {
            log.error("Failed to update message statuses to fetch received messages; \n No results were returned by the query: {}", e.getMessage());
        }
    }

    private void handleSeenMessagesType(WsActionRequestDTO requestDTO) {
        try {
            int affectedRows = messageService.updateMessageStatusesToSeenBetweenUsers(
                    requestDTO.getTransmitter(), requestDTO.getReceiver());
            log.info("Updated {} messages to SEEN status", affectedRows);
        } catch (Exception e) {
            log.error("Failed to update SEEN status: {}", e.getMessage());
        }
    }

    private void handleSeenSignalType(WsActionRequestDTO requestDTO) {
        try {
            WsActionResponseDTO seenSignalResponse = WsActionResponseDTO.builder()
                    .type(ActionType.SEEN_SIGNAL)
                    .transmitter(requestDTO.getReceiver())  // Switched transmitter/receiver
                    .receiver(requestDTO.getTransmitter())
                    .build();

            if (webSocketService.hasSession(requestDTO.getTransmitter())) {
                webSocketService.sendMessageToKey(
                        requestDTO.getTransmitter(),
                        objectMapper.writeValueAsString(seenSignalResponse));
            }
        } catch (Exception e) {
            log.error("Failed to notify SEEN_SIGNAL: {}", e.getMessage());
        }
    }
}
