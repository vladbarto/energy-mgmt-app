//package ro.tucn.energy_mgmt_login.config.WebSocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//import ro.tucn.energy_mgmt_login.security.interceptor.JwtHandshakeInterceptor;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
//    private final CustomWebSocketHandler customWebSocketHandler;
//
//    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor, CustomWebSocketHandler customWebSocketHandler) {
//        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
//        this.customWebSocketHandler = customWebSocketHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(customWebSocketHandler, "/ws")
//                .addInterceptors(jwtHandshakeInterceptor)
//                .setAllowedOrigins("*"); // Restrict origins in production
//    }
//}
