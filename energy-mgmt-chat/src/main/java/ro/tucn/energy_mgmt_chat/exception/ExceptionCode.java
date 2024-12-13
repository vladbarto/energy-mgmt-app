package ro.tucn.energy_mgmt_chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    ERR001_NO_MESSAGE_FOR_USER_ID_FOUND("No message found received by user with userId %s");
    private final String message;
}
