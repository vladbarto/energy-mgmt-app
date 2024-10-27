package ro.tucn.energy_mgmt_devices.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible exceptions
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    ERR002_USERNAME_NOT_FOUND("Username %s not found"),
    ERR099_INVALID_CREDENTIALS("Invalid credentials."),
    ERR003_DEVICE_OF_USERID_NOT_FOUND("Device of user with ID %s not found");
    private final String message;
}
