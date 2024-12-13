package ro.tucn.energy_mgmt_chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import ro.tucn.energy_mgmt_chat.mapper.MessageMapper;
import ro.tucn.energy_mgmt_chat.repository.MessageRepository;
import ro.tucn.energy_mgmt_chat.service.WebSocket.WebSocketService;
import ro.tucn.energy_mgmt_chat.service.WebSocket.WebSocketServiceBean;
import ro.tucn.energy_mgmt_chat.service.message.MessageService;
import ro.tucn.energy_mgmt_chat.service.message.MessageServiceBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class Config {
    @Bean
    public MessageService messageServiceBean(
            MessageRepository messageRepository,
            MessageMapper messageMapper,
            @Value("${spring.application.name}") String applicationName
    ) {
        return new MessageServiceBean(messageRepository, messageMapper, applicationName);
    }

    @Bean
    public Map<String, WebSocketSession> sessionMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public WebSocketService webSocketServiceBean(
            Map<String, WebSocketSession> sessionMap,
            @Value("${spring.application.name}") String applicationName
    ) {
        return new WebSocketServiceBean(
                sessionMap,
                applicationName
        );
    }
}
