package ro.tucn.energy_mgmt_chat.dto.message;

public enum MessageStatus {
    SENT,
    RECEIVED, // like on whatsapp, the user has internet and received the notification, but hasn't seen it yet
    SEEN
}
