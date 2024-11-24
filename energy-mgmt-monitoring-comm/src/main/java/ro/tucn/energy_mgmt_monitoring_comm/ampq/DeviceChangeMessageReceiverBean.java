package ro.tucn.energy_mgmt_monitoring_comm.ampq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import ro.tucn.energy_mgmt_monitoring_comm.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.service.device.DeviceService;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceChangeMessageReceiverBean implements MessageReceiver {

    private final DeviceService deviceService;
    private final ObjectMapper objectMapper;
    
    @Override
    @RabbitListener(
            queues = "${spring.rabbitmq.from-device.queue}",
            containerFactory = "deviceRabbitListenerContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);

        try {
            DeviceRequestDTO deviceRequestDTO = objectMapper.readValue(message, DeviceRequestDTO.class);

            deviceService.processMessage(deviceRequestDTO);

        } catch (Exception e) {
            log.error("Message error: {}", e.getMessage());
        }
    }
}
