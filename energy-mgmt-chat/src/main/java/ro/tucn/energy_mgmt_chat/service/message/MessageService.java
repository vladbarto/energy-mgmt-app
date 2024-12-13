package ro.tucn.energy_mgmt_chat.service.message;

import ro.tucn.energy_mgmt_chat.dto.message.MessageRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDTO save(MessageRequestDTO messageRequestDTO);
    int updateMessageStatusesToReceived(UUID clientId);
    int updateMessageStatusesToSeenBetweenUsers(UUID transmitterId, UUID receiverId);
    List<MessageResponseDTO> getMessagesBetweenUsers(UUID aUserId, UUID bUserId);
}
