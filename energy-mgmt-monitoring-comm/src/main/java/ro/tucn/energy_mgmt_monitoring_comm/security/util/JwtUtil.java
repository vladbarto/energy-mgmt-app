package ro.tucn.energy_mgmt_monitoring_comm.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Properties;

@Slf4j
@UtilityClass
public class JwtUtil {

    public String secretKey;
    public Integer tokenExpirationDays;

    static {
        try (InputStream inputStream = JwtUtil.class.getResourceAsStream("/application.yaml")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            JwtUtil.secretKey = properties.getProperty("secret-key");
            JwtUtil.tokenExpirationDays = Integer.parseInt(properties.getProperty("token-expiration-days"));
        } catch (IOException | NumberFormatException e) {
            log.error(e.getMessage());
        }
    }
    public String extractUsernameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject(); // The 'sub' claim contains the username
        } catch (Exception e) {
            return null; // Invalid token
        }
    }

    public Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

