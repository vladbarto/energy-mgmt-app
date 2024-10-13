package ro.tucn.energy_mgmt_backend.controller;

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

import ro.tucn.energy_mgmt_backend.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_backend.exception.ExceptionBody;
import ro.tucn.energy_mgmt_backend.service.UserService;
import ro.tucn.energy_mgmt_backend.dto.user.UserRequestDTO;

import java.util.List;

@Slf4j
@RestController
//@CrossOrigin("*")//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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
    public ResponseEntity<UserResponseDTO> getLoggedUserInfo() {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = new String("vladbarto");//(String) authentication.getPrincipal();

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
}
