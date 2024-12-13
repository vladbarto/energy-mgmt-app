package ro.tucn.energy_mgmt_login.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
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

    public String generateJwtToken(String username, Collection<? extends GrantedAuthority> role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role.stream().map(GrantedAuthority::getAuthority).toList())
                .expiration(JwtUtil.getExpirationDate(JwtUtil.tokenExpirationDays))
                .signWith(JwtUtil.getSigningKey(JwtUtil.secretKey))
                .compact();
    }

    public Cookie buildCookie(String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
//        cookie.setMaxAge(JwtUtil.tokenExpirationDays);
        cookie.setMaxAge((int) JwtUtil.getExpirationDate(JwtUtil.tokenExpirationDays).getTime());

        return cookie;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization") == null
                ? null
                : request.getHeader("Authorization").split("Bearer ")[1];
    }

    private Date getExpirationDate(Integer tokenExpirationDays) {
        return Date.valueOf(LocalDate.now().plusDays(tokenExpirationDays));
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