package ro.tucn.energy_mgmt_devices.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ro.tucn.energy_mgmt_devices.exception.ExceptionBody;
import ro.tucn.energy_mgmt_devices.service.device.DeviceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
//@CrossOrigin("*")//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/device/v1")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chef found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Chef not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
    public ResponseEntity<List<DeviceResponseDTO>> findAll() {
        return new ResponseEntity<>(
                deviceService.findAll(),
                HttpStatus.OK
        );
    }

//    @GetMapping("/info")
//    public ResponseEntity<DeviceResponseDTO> getLoggedDeviceInfo() {
//        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String devicename = new String("vladbarto");//(String) authentication.getPrincipal();
//
//        return new ResponseEntity<>(
//                deviceService.findByDeviceName(devicename),
//                HttpStatus.OK
//        );
//    }

    @PostMapping("/one")
    @Operation(summary = "Device Registration")
    @ApiResponse(responseCode = "201", description = "Device successfully registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))})
    public ResponseEntity<DeviceResponseDTO> saveDevice(
            @RequestBody DeviceRequestDTO deviceRequestDTO
    ) {
        return new ResponseEntity<>(
                deviceService.save(deviceRequestDTO),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update one device")
    @ApiResponse(responseCode = "301", description = "Device successfully updated",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @RequestBody DeviceRequestDTO deviceRequestDTO, @PathVariable("id") UUID deviceId
    ) {
        return new ResponseEntity<>(
                deviceService.update(deviceRequestDTO, deviceId),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete one device")
    @ApiResponse(responseCode = "301", description = "device successfully deleted",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponseDTO> deleteById(@PathVariable("id") UUID deviceId) {
        return new ResponseEntity<>(
                deviceService.deleteById(deviceId),
                HttpStatus.OK
        );
    }
}
