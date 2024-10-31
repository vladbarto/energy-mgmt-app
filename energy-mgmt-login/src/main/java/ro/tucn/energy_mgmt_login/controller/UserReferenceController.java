package ro.tucn.energy_mgmt_login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tucn.energy_mgmt_login.dto.userReference.UserReferenceResponseDTO;
import ro.tucn.energy_mgmt_login.service.userReference.UserReferenceService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/userRef/v1")
@CrossOrigin(origins = {"http://localhost:4200", "http://front_app:4200", "http://localhost:6581"}, allowCredentials = "true")
@RequiredArgsConstructor
public class UserReferenceController {
    private final UserReferenceService userReferenceService;

    @DeleteMapping("/{id}")
    public ResponseEntity<UserReferenceResponseDTO> delete(@PathVariable("id") UUID userId) {
        return new ResponseEntity<>(
                userReferenceService.delete(userId),
                HttpStatus.OK
        );
    }
}
