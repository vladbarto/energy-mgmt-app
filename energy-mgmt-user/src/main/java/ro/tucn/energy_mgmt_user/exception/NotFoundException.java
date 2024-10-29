package ro.tucn.energy_mgmt_user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Object not found in DB
 */
@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {

    private final String message;
}
