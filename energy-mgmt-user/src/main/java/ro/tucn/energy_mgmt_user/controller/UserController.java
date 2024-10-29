package ro.tucn.energy_mgmt_user.controller;

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

import ro.tucn.energy_mgmt_user.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_user.exception.ExceptionBody;
import ro.tucn.energy_mgmt_user.service.UserService;
import ro.tucn.energy_mgmt_user.dto.user.UserRequestDTO;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chef found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Chef not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return new ResponseEntity<>(
                userService.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponseDTO> getLoggedUserInfo(@RequestParam String username) {
        log.info("Received username from request param: " + username);

        return new ResponseEntity<>(
                userService.findByUsername(username),
                HttpStatus.OK
        );
    }

    @PostMapping("/one")
    @Operation(summary = "User Registration")
    @ApiResponse(responseCode = "201", description = "User successfully registered",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))})
    public ResponseEntity<UserResponseDTO> saveUser(
            @RequestBody UserRequestDTO userRequestDTO
    ) {
        return new ResponseEntity<>(
                userService.save(userRequestDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{username}")
    @Operation(summary = "Gets one specific user", description = "the user must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionBody.class))})
    })
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(
                userService.findByUsername(username),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update one user")
    @ApiResponse(responseCode = "301", description = "User successfully updated",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @RequestBody UserRequestDTO userRequestDTO, @PathVariable("id") UUID userId
    ) {
        return new ResponseEntity<>(
                userService.update(userRequestDTO, userId),
                HttpStatus.OK
        );
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete one user")
    @ApiResponse(responseCode = "301", description = "user successfully deleted",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))})
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> deleteById(@PathVariable("id") UUID userId) {
        return new ResponseEntity<>(
                userService.deleteById(userId),
                HttpStatus.OK
        );
    }
}
