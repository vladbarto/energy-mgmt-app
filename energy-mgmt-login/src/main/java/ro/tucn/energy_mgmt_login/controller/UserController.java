package ro.tucn.energy_mgmt_login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.tucn.energy_mgmt_login.dto.user.UserRequestDTO;
import ro.tucn.energy_mgmt_login.dto.user.UserResponseDTO;
import ro.tucn.energy_mgmt_login.service.user.UserService;

@Slf4j
@RestController
@RequestMapping("/user/v1")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {
    private final UserService userServiceBean;

    @GetMapping("info")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername(username);

        return new ResponseEntity<>(
                userServiceBean.getInfo(request),
                HttpStatus.OK
        );
    }
}
