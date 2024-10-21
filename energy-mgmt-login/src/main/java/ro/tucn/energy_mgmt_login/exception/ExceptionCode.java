package ro.tucn.energy_mgmt_login.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible exceptions
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    ERR001_INVALID_CREDENTIALS("Invalid credentials.");

    private final String message;
}