package ro.tucn.energy_mgmt_chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.tucn.energy_mgmt_chat.dto.message.MessageStatus;
import ro.tucn.energy_mgmt_chat.model.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    @Modifying
    @Query(value = "UPDATE MESSAGE SET STATUS = 'RECEIVED' WHERE RECEIVER = :sender AND STATUS = 'SENT'", nativeQuery = true)
    int updateAllBySenderWithStatusSentToReceived(@Param("sender") String sender);

    @Modifying
    @Query(value = "UPDATE MESSAGE SET STATUS = :status WHERE TRANSMITTER = :transmitter AND RECEIVER = :receiver AND STATUS <> :status", nativeQuery = true)
    int updateStatusesToSeen(@Param("transmitter") String transmitter,
                             @Param("receiver") String receiver,
                             @Param("status") String status);

    @Query("SELECT m FROM MessageEntity m WHERE (m.transmitter = :userA AND m.receiver = :userB) OR (m.transmitter = :userB AND m.receiver = :userA)")
    List<MessageEntity> findAllMessagesBetweenUsers(@Param("userA") String userA, @Param("userB") String userB);

    @Query("SELECT m FROM MessageEntity m WHERE (m.receiver = :announcementsUsername)")
    List<MessageEntity> findAllMessagesWithReceiverCalledAnnouncements(
            @Param("announcementsUsername") String announcementsUsername);
}
