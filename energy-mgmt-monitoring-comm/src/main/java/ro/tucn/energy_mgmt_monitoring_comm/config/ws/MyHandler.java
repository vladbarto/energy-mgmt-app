package ro.tucn.energy_mgmt_monitoring_comm.config.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ro.tucn.energy_mgmt_monitoring_comm.dto.chartData.ChartDataResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.MessageType;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.TextMessageRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.TextMessageResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingService;
import ro.tucn.energy_mgmt_monitoring_comm.service.webSocket.WebSocketService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
        webSocketService.addSession(session);
        log.info("WebSocket connection established.");
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
                // Calculate the sum of differences and populate the map

                float cumulativeSum = 0; // Initialize cumulative sum
                for (int i = 0; i < response.size() - 1; i++) {
                    ReadingResponseDTO currentReading = response.get(i);
                    ReadingResponseDTO nextReading = response.get(i + 1);

                    if(nextReading.getTimestamp().getHours() - currentReading.getTimestamp().getHours() > 0)
                    {
                        // if those readings belong to different hour sets
                        responseMap.put(currentReading.getTimestamp().getHours(), cumulativeSum);
                        cumulativeSum = 0;
                    }
                    else {
                        // Calculate the difference between the current and next reading
                        float difference = Math.abs(nextReading.getReadValue() - currentReading.getReadValue());
                        cumulativeSum += difference;
                    }
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
        webSocketService.removeSession();
        log.info("WebSocket connection closed.");
    }
}
