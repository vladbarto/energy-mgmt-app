package ro.tucn.energy_mgmt_login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_login.service.user.UserService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user/v1")
@CrossOrigin(origins = {"http://localhost:4200", "http://front_app:4200"}, allowCredentials = "true")
//@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername(username);

        return new ResponseEntity<>(
                userService.getInfo(request),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    //@PreAuthorize("authentication.principal.isAdmin")
    public ResponseEntity<List<UserResponseDTO>> getAll() {

        return new ResponseEntity<>(
                userService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(
                userService.getByUsername(username),
                HttpStatus.OK
        );
    }

    @PostMapping("/one")
    public ResponseEntity<UserResponseDTO> saveOne(@RequestBody UserRequestDTO request) {
        return new ResponseEntity<>(
                userService.save(request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteById(@PathVariable("id") UUID userId) {
        return new ResponseEntity<>(
                userService.deleteById(userId),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable("id") UUID userId, @RequestBody UserRequestDTO request) {
        return new ResponseEntity<>(
                userService.updateById(userId, request),
                HttpStatus.OK
        );
    }
}
