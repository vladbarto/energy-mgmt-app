//package ro.tucn.energy_mgmt_login.security.interceptor;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//import ro.tucn.energy_mgmt_login.security.util.JwtUtil;
//
//import java.util.Map;
//
//@Component
//public class JwtHandshakeInterceptor implements HandshakeInterceptor {
//
//    @Override
//    public boolean beforeHandshake(
//            ServerHttpRequest request,
//            ServerHttpResponse response,
//            WebSocketHandler wsHandler,
//            Map<String, Object> attributes
//    ) {
//        try {
//            HttpHeaders headers = request.getHeaders();
//            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String token = authHeader.substring(7);
//                Claims claims = JwtUtil.parseToken(token);
//                attributes.put("user", claims.getSubject());
//                attributes.put("roles", claims.get("role"));
//                return true;
//            }
//        } catch (Exception e) {
//            response.setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
//        }
//        return false;
//    }
//
//    @Override
//    public void afterHandshake(
//            ServerHttpRequest request,
//            ServerHttpResponse response,
//            WebSocketHandler wsHandler,
//            Exception ex
//    ) {
//        // Optional: Post-handshake logic
//    }
//}
