package ro.tucn.energy_mgmt_chat.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ro.tucn.energy_mgmt_chat.dto.message.MessageRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageResponseDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageStatus;
import ro.tucn.energy_mgmt_chat.exception.NotFoundException;
import ro.tucn.energy_mgmt_chat.exception.ExceptionCode;
import ro.tucn.energy_mgmt_chat.mapper.MessageMapper;
import ro.tucn.energy_mgmt_chat.model.MessageEntity;
import ro.tucn.energy_mgmt_chat.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MessageServiceBean implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final String applicationName;

    @Override
    @Transactional
    public MessageResponseDTO save(MessageRequestDTO messageRequestDTO) {
        log.info("Saving incoming message {} for application [{}]", messageRequestDTO, applicationName);

        try {
            log.debug("Mapping MessageRequestDTO to MessageEntity");
            MessageEntity messageToBeSaved = messageMapper.requestDTOToEntity(messageRequestDTO);
            log.debug("MessageEntity after mapping: {}", messageToBeSaved);

            log.debug("Saving MessageEntity to repository");
            MessageEntity messageSaved = messageRepository.save(messageToBeSaved);
            log.debug("MessageEntity saved: {}", messageSaved);

            MessageResponseDTO responseDTO = messageMapper.entityToResponseDTO(messageSaved);
            log.debug("Mapped MessageEntity to MessageResponseDTO: {}", responseDTO);

            return responseDTO;
        } catch (Exception e) {
            log.error("Error while saving message: {}", messageRequestDTO, e);
            throw e; // Optional: wrap in a custom exception if needed
        }
    }

    @Override
    @Transactional
    public int updateMessageStatusesToReceived(UUID clientId) {
        return messageRepository.updateAllBySenderWithStatusSentToReceived(clientId);
    }

    @Override
    @Transactional
    public int updateMessageStatusesToSeenBetweenUsers(UUID transmitter, UUID receiver) {
        log.info("Updating message statuses to SEEN between transmitter [{}] and receiver [{}]", transmitter, receiver);

        try {
            int updatedCount = messageRepository.updateStatusesToSeen(transmitter, receiver, MessageStatus.SEEN);
            log.info("Updated {} messages to SEEN between transmitter [{}] and receiver [{}]", updatedCount, transmitter, receiver);
            return updatedCount;
        } catch (Exception e) {
            log.error("Error updating message statuses to SEEN between transmitter [{}] and receiver [{}]", transmitter, receiver, e);
            throw e; // Optional: wrap in a custom exception if needed
        }
    }


    @Override
    public List<MessageResponseDTO> getMessagesBetweenUsers(UUID aUserId, UUID bUserId) {
        log.info("Getting messages between {} and {}", aUserId, bUserId);

        return messageMapper.entityListToResponseDTOList(
                messageRepository.findAllMessagesBetweenUsers(aUserId, bUserId)
        );
    }
}
