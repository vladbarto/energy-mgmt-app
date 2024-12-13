package ro.tucn.energy_mgmt_chat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ro.tucn.energy_mgmt_chat.dto.message.MessageRequestDTO;
import ro.tucn.energy_mgmt_chat.dto.message.MessageResponseDTO;
import ro.tucn.energy_mgmt_chat.model.MessageEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper extends GenericMapper<MessageEntity, MessageRequestDTO, MessageResponseDTO> {
}
