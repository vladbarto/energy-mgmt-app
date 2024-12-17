package ro.tucn.energy_mgmt_chat.service.message;

import ro.tucn.energy_mgmt_chat.dto.message.MessageRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageResponseDTO;

import java.util.List;

public interface MessageService {
    MessageResponseDTO save(MessageRequestDTO messageRequestDTO);
    int updateMessageStatusesToReceived(String clientUsername);
    int updateMessageStatusesToSeenBetweenUsers(String transmitter, String receiver);
    List<MessageResponseDTO> getMessagesBetweenUsers(String aUsername, String anotherUsername);
    List<MessageResponseDTO> getAnnouncements(String anotherUsername);
}
