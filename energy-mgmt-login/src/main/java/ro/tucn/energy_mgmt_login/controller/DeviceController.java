package ro.tucn.energy_mgmt_login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tucn.energy_mgmt_login.dto.device.DeviceRequestDTO;
import ro.tucn.energy_mgmt_login.dto.device.DeviceResponseDTO;

import ro.tucn.energy_mgmt_login.exception.ExceptionBody;
import ro.tucn.energy_mgmt_login.service.device.DeviceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/device/v1")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceServiceBean;

    @PostMapping("/one")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<DeviceResponseDTO> saveDevice(@RequestBody DeviceRequestDTO request) {

        return new ResponseEntity<>(
                deviceServiceBean.saveDevice(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all")
    @Operation(summary = "Gets all devices", description = "none")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Devices not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
    //@PreAuthorize("authentication.principal.isAdmin")
    public ResponseEntity<List<DeviceResponseDTO>> findAll() {

        return new ResponseEntity<>(
                deviceServiceBean.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/user")
    @Operation(summary = "Gets all devices belonging to given userId", description = "the user must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Devices not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
    //@PreAuthorize("authentication.principal.isAdmin")
    public ResponseEntity<List<DeviceResponseDTO>> findAllByUserId(@RequestParam("id") UUID userId) {

        return new ResponseEntity<>(
                deviceServiceBean.findAllByUserId(userId),
                HttpStatus.OK
        );
    }

}
