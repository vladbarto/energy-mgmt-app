package ro.tucn.energy_mgmt_monitoring_comm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible exceptions
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    ERR004_DEVICE_NOT_FOUND("Device with ID %s not found");
    private final String message;
}