package ro.tucn.energy_mgmt_monitoring_comm.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.tucn.energy_mgmt_monitoring_comm.security.util.JwtUtil;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter {

    public static boolean validateToken(String token) {
        try {
            JwtUtil.parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
