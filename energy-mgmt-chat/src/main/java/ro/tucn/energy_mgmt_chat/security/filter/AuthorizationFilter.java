package ro.tucn.energy_mgmt_chat.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.tucn.energy_mgmt_chat.security.util.JwtUtils;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter {

    public static boolean validateToken(String token) {
        try {
            JwtUtils.parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
