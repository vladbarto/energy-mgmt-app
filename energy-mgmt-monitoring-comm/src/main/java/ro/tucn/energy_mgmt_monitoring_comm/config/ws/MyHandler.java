package ro.tucn.energy_mgmt_monitoring_comm.config.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ro.tucn.energy_mgmt_monitoring_comm.dto.chartData.ChartDataResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.MessageType;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.TextMessageRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.TextMessageResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingService;
import ro.tucn.energy_mgmt_monitoring_comm.service.webSocket.WebSocketService;
import static ro.tucn.energy_mgmt_monitoring_comm.config.ws.wsUtils.extractQueryParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ReadingService readingService;
    private final WebSocketService webSocketService;

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
            // "request" is received and mapped to an object
            TextMessageRequestDTO textMessageRequestDTO = objectMapper.readValue(message.getPayload(), TextMessageRequestDTO.class);

            // if we identify the message as a FETCH_READINGS "request"
            if(textMessageRequestDTO.getType().equals(MessageType.FETCH_READINGS)) {
                log.info("Received FETCH_READINGS message: {}", textMessageRequestDTO);

                // query the DB for all readings for given deviceId on given date
                List<ReadingResponseDTO> response = readingService.findAllByIdAndDate(
                        textMessageRequestDTO.getDeviceId(),
                        textMessageRequestDTO.getDate()
                );

                // a map for hourly consumption of type <Hour, ReadValue>
                Map<Integer, Float> responseMap = new HashMap<>();

                for (int i = 0; i < 24; i++) {
                    responseMap.put(i, 0.0f); // Default value for each hour is 0.0
                }

                if (!response.isEmpty()) {
                    float cumulativeSum = 0; // Initialize cumulative sum
                    int currentHour = response.get(0).getTimestamp().toLocalDateTime().getHour(); // Start with the hour of the first reading

                    for (int i = 0; i < response.size() - 1; i++) {
                        ReadingResponseDTO currentReading = response.get(i);
                        ReadingResponseDTO nextReading = response.get(i + 1);

                        int nextHour = nextReading.getTimestamp().toLocalDateTime().getHour();

                        // Calculate the difference and add to the cumulative sum
                        float difference = Math.abs(nextReading.getReadValue() - currentReading.getReadValue());
                        cumulativeSum += difference;

                        // If the next reading belongs to a different hour, store the cumulative sum for the current hour
                        if (nextHour != currentHour) {
                            responseMap.put(currentHour, cumulativeSum);
                            cumulativeSum = 0; // Reset cumulative sum
                            currentHour = nextHour; // Update current hour
                        }
                    }

                    // Handle the last reading explicitly
                    ReadingResponseDTO lastReading = response.get(response.size() - 1);
                    int lastHour = lastReading.getTimestamp().toLocalDateTime().getHour();

                    // Add the remaining cumulative sum to the last hour
                    responseMap.put(lastHour, cumulativeSum);
                }

                // Convert the map to a list of ChartDataResponseDTO
                List<ChartDataResponseDTO> chartDataList = responseMap.entrySet()
                        .stream()
                        .map(entry -> ChartDataResponseDTO.builder()
                                .hour(entry.getKey())
                                .readValue(entry.getValue())
                                .build())
                        .collect(Collectors.toList());

                // build the response
                TextMessageResponseDTO responseMessage = TextMessageResponseDTO.builder()
                        .type(MessageType.BUNCH_OF_READINGS)
                        .message(objectMapper.writeValueAsString(chartDataList))
                        .date(textMessageRequestDTO.getDate())
                        .deviceId(textMessageRequestDTO.getDeviceId())
                        .build();

                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseMessage)));
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
