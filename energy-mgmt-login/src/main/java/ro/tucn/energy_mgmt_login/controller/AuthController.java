package ro.tucn.energy_mgmt_login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls the login of a user
 */
@Slf4j
@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://front_app:4200",
        "http://localhost:6581",
        "http://localhost"}, allowCredentials = "true")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Login attempt")
    @ApiResponse(responseCode = "201", description = "Logged in successfully")
    public ResponseEntity<Void> login() {
        log.info("Login request detected...");

        return ResponseEntity.ok().build();
    }
}
