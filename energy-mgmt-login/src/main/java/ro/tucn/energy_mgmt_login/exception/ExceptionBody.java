package ro.tucn.energy_mgmt_login.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Exception returned for a request: type of exception and timestamp
 */
@Getter
@RequiredArgsConstructor
public class ExceptionBody {

    private final String message;
    private final LocalDateTime timestamp;

    public ExceptionBody(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
