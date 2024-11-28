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
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.userRef.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_devices.exception.ExceptionBody;
import ro.tucn.energy_mgmt_devices.service.userRef.UserReferenceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost"}, allowCredentials = "true")
@RequestMapping("/userRef/v1")
@RequiredArgsConstructor
public class UserReferenceController {

    private final UserReferenceService userReferenceService;

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "userreference found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserReferenceResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "userreference not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
    public ResponseEntity<List<UserReferenceResponseDTO>> findAll() {
        return new ResponseEntity<>(
                userReferenceService.findAll(),
                HttpStatus.OK
        );
    }

//    @GetMapping("/info")
//    public ResponseEntity<UserReferenceResponseDTO> getLoggedUserReferenceInfo() {
//        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userReferencename = new String("vladbarto");//(String) authentication.getPrincipal();
//
//        return new ResponseEntity<>(
//                userReferenceService.findByUserReferenceName(userReferencename),
//                HttpStatus.OK
//        );
//    }

    @PostMapping("/one")
    @Operation(summary = "UserReference Registration")
    @ApiResponse(responseCode = "201", description = "UserReference successfully registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserReferenceResponseDTO.class))})
    public ResponseEntity<UserReferenceResponseDTO> saveUserReference(
            @RequestBody UserReferenceRequestDTO userReferenceRequestDTO
    ) {
        return new ResponseEntity<>(
                userReferenceService.save(userReferenceRequestDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update one userReference")
    @ApiResponse(responseCode = "301", description = "UserReference successfully updated",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserReferenceResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserReferenceResponseDTO> updateUserReference(
            @RequestBody UserReferenceRequestDTO userReferenceRequestDTO, @PathVariable("id") UUID userReferenceId
    ) {
        return new ResponseEntity<>(
                userReferenceService.update(userReferenceRequestDTO, userReferenceId),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete one userReference")
    @ApiResponse(responseCode = "301", description = "userReference successfully deleted",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserReferenceResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserReferenceResponseDTO> deleteById(@PathVariable("id") UUID userReferenceId) {
        return new ResponseEntity<>(
                userReferenceService.deleteById(userReferenceId),
                HttpStatus.OK
        );
    }
}
