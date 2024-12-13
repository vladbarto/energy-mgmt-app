package ro.tucn.energy_mgmt_chat.config.WebSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ro.tucn.energy_mgmt_chat.service.WebSocket.WebSocketService;
import ro.tucn.energy_mgmt_chat.service.message.MessageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ro.tucn.energy_mgmt_chat.config.WebSocket.wsUtils.extractQueryParam;

@Slf4j
@RequiredArgsConstructor
public class MyHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;
    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            // Extract userId from the WebSocket URI query parameters.
            String query = session.getUri().getQuery();
            String userId = extractQueryParam(query, "userId");
            if (userId == null || userId.isEmpty()) {
                log.warn("Missing userId in WebSocket connection.");
                session.close(CloseStatus.BAD_DATA.withReason("Missing userId"));
                return;
            }

            // Register this session with the userId in the WebSocketService.
            webSocketService.addSession(userId, session);
            log.info("WebSocket connection established for userId: {}", userId);

        } catch (Exception e) {
            log.error("Failed to establish WebSocket connection: {}", e.getMessage());
            try {
                session.close(CloseStatus.SERVER_ERROR.withReason("Failed to process userId"));
            } catch (IOException ioException) {
                log.error("Failed to close WebSocket session: {}", ioException.getMessage());
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
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
        try {
            String query = session.getUri().getQuery();
            String userId = extractQueryParam(query, "userId");
            if (userId != null) {
                webSocketService.removeSession(userId);
                log.info("WebSocket connection closed for userId: {}", userId);
            }
        } catch (Exception e) {
            log.error("Failed to process WebSocket disconnection: {}", e.getMessage());
        }
    }

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
            if(webSocketService.hasSession(requestDTO.getReceiver().toString())) {
                WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
                        .transmitter(requestDTO.getTransmitter())
                        .receiver(requestDTO.getReceiver())
                        .text(requestDTO.getText())
                        .type(ActionType.RECEIVE_MESSAGE)
                        .status(MessageStatus.SENT)
                        .messageId(messageResponseDTO.getId())
                        .build();

                log.info("Sending out typing notification to connected receiver: {}", wsActionResponseDTO);
                webSocketService.sendMessageToKey(
                        requestDTO.getReceiver().toString(),
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
            if(webSocketService.hasSession(responseDTO.getReceiver().toString())) {
                log.info("Sending out typing notification to connected receiver: {}", responseDTO);
                webSocketService.sendMessageToKey(responseDTO.getReceiver().toString(), objectMapper.writeValueAsString(responseDTO));
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleFetchMessagesType(WsActionRequestDTO requestDTO) {
        UUID whoMakesTheRequest = requestDTO.getTransmitter();
        UUID whoHeHasAConversationWith = requestDTO.getReceiver();

        List<MessageResponseDTO> messageResponseDTOList = messageService.getMessagesBetweenUsers(
                whoMakesTheRequest,
                whoHeHasAConversationWith);

        try {
            log.info("Fetching messages from receiver: {}", requestDTO.getReceiver());

            for (MessageResponseDTO messageResponseDTO : messageResponseDTOList) {
                WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
                        .type(ActionType.FETCH_MESSAGES)
                        .transmitter(messageResponseDTO.getTransmitter())
                        .receiver(messageResponseDTO.getReceiver())
                        .status(messageResponseDTO.getStatus())
                        .text(messageResponseDTO.getText())
                        .messageId(messageResponseDTO.getId())
                        //.sendingTime(messageResponseDTO.getSendingTime())
                        .build();

                if(webSocketService.hasSession(wsActionResponseDTO.getReceiver().toString())) {
                    log.info("Sending message to who solicited it: {}", wsActionResponseDTO);
                    webSocketService.sendMessageToKey(whoMakesTheRequest.toString(), objectMapper.writeValueAsString(wsActionResponseDTO));
                }
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


//    private void handleSeenSignalType(WsActionRequestDTO requestDTO) {
//        List<MessageResponseDTO> messageResponseDTOList = messageService.getMessagesBetweenUsers(
//                requestDTO.getTransmitter(), requestDTO.getReceiver());
//
//        try {
//            for (MessageResponseDTO messageResponseDTO : messageResponseDTOList) {
//                WsActionResponseDTO wsActionResponseDTO = WsActionResponseDTO.builder()
//                        .type(ActionType.SEEN_SIGNAL)
//                        .transmitter(messageResponseDTO.getTransmitter())
//                        .receiver(messageResponseDTO.getReceiver())
//                        .status(messageResponseDTO.getStatus())
//                        .text(messageResponseDTO.getText())
//                        .messageId(messageResponseDTO.getId())
//                        //.sendingTime(messageResponseDTO.getSendingTime())
//                        .build();
//
//                if(webSocketService.hasSession(wsActionResponseDTO.getReceiver().toString())) {
//                    log.info("Updated msg from msg list with seen status: {}", wsActionResponseDTO);
//                    webSocketService.sendMessageToKey(
//                            wsActionResponseDTO.getReceiver().toString(),
//                            objectMapper.writeValueAsString(wsActionResponseDTO));
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
    private void handleSeenSignalType(WsActionRequestDTO requestDTO) {
        try {
            WsActionResponseDTO seenSignalResponse = WsActionResponseDTO.builder()
                    .type(ActionType.SEEN_SIGNAL)
                    .transmitter(requestDTO.getReceiver())  // Switched transmitter/receiver
                    .receiver(requestDTO.getTransmitter())
                    .build();

            if (webSocketService.hasSession(requestDTO.getTransmitter().toString())) {
                webSocketService.sendMessageToKey(
                        requestDTO.getTransmitter().toString(),
                        objectMapper.writeValueAsString(seenSignalResponse));
            }
        } catch (Exception e) {
            log.error("Failed to notify SEEN_SIGNAL: {}", e.getMessage());
        }
    }

}