package ro.tucn.energy_mgmt_chat.config.WebSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ro.tucn.energy_mgmt_chat.security.interceptor.JwtHandshakeInterceptor;
import ro.tucn.energy_mgmt_chat.service.WebSocket.WebSocketService;
import ro.tucn.energy_mgmt_chat.service.message.MessageService;

@Slf4j
@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketService webSocketService;
    private final MessageService messageService;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("Registering websocket handlers");
        registry.addHandler(myHandler(), "/wsChat")
//                .addInterceptors(new JwtHandshakeInterceptor())  // Add JWT authentication interceptor
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler(new ObjectMapper(), webSocketService, messageService);
    }

}