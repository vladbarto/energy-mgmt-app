package ro.tucn.energy_mgmt_monitoring_comm.ampq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.MessageType;
import ro.tucn.energy_mgmt_monitoring_comm.dto.textMessage.TextMessageResponseDTO;
import ro.tucn.energy_mgmt_monitoring_comm.service.device.DeviceService;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingService;
import ro.tucn.energy_mgmt_monitoring_comm.service.webSocket.WebSocketService;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceConsumptionMessageReceiverBean implements MessageReceiver {

    private final WebSocketService webSocketService;
    private final ReadingService readingService;
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper;

    /***
     * This method receives message from simulator via a Direct Queue and handles them
     * @param message received on queue from simulator
     */
    @Override
    @RabbitListener(
            queues = "${spring.rabbitmq.simulator.queue}",
            containerFactory = "simulatorListenerContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received message from queue: {}", message);

        try {
            ReadingRequestDTO reading = objectMapper.readValue(message, ReadingRequestDTO.class);

            // Save the reading into Monitoring DB
            readingService.save(reading);

            // get the reference mhec for comparison
            DeviceResponseDTO correspondingDevice = deviceService.findDevice(reading.getDeviceId());
            String username = correspondingDevice.getUsername();
            float mhec = correspondingDevice.getMhec();

            if(reading.getReadValue() > mhec) { // if read value exceeds mhec
                // Send notification to the global WebSocket session
                String notification = String.format(
                        "Value: %f - MHEC exceeded",
                        reading.getReadValue()
                );

                TextMessageResponseDTO responseDTO = TextMessageResponseDTO.builder()
                        .type(MessageType.PUSH_NOTIFICATION)
                        .deviceId(reading.getDeviceId())
                        .message(notification)
                        .date(reading.getTimestamp().toString())
                        .build();

                if (webSocketService.hasSession(username)) {
                    webSocketService.sendMessageToKey(username, objectMapper.writeValueAsString(responseDTO));
                }
                else {
                    log.warn("No active WebSocket session for user {} to send notification.", username);
                }
            }
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
