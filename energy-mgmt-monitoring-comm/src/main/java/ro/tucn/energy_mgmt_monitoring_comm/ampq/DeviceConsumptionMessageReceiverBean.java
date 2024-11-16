package ro.tucn.energy_mgmt_monitoring_comm.ampq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ro.tucn.energy_mgmt_monitoring_comm.dto.readings.ReadingRequestDTO;
import ro.tucn.energy_mgmt_monitoring_comm.service.readings.ReadingService;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceConsumptionMessageReceiverBean implements MessageReceiver {

    private final ReadingService readingService;
    private final ObjectMapper objectMapper;

    @Override
    @RabbitListener(
            queues = "${spring.rabbitmq.simulator.queue}",
            containerFactory = "simulatorListenerContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);

        try {
            ReadingRequestDTO readingRequestDTO = objectMapper.readValue(message, ReadingRequestDTO.class);

            readingService.save(readingRequestDTO);
        } catch (Exception e) {
            log.error("Message error: {}", e.getMessage());
        }
    }
}
