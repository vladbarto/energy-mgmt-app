package ro.tucn.energy_mgmt_devices.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.device.DeviceResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;
import ro.tucn.energy_mgmt_devices.exception.ExceptionBody;
import ro.tucn.energy_mgmt_devices.service.device.DeviceService;
import ro.tucn.energy_mgmt_devices.service.rabbitmq.RabbitMqService;


@Slf4j
@RestController
@RequestMapping("/rabbit/v1")
@RequiredArgsConstructor
/**
 * dummy controller, test-purpose only
 * to see if the queue is created
 */
public class RabbitMqController {
    private final RabbitMqService rabbitMqService;

    @PostMapping("/hello")
    public ResponseEntity<DeviceChangeResponseDTO> dummyQueueMessage(
            @RequestBody DeviceChangeRequestDTO deviceChangeRequestDTO
    ) {
        log.info("Nou mesaj pe coada cu deviceId: " + deviceChangeRequestDTO.getDeviceId());

        return new ResponseEntity<>(
                rabbitMqService.transmitMessage(deviceChangeRequestDTO ),
                HttpStatus.OK
        );
    }
}
