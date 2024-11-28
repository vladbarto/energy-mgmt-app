package ro.tucn.energy_mgmt_monitoring_comm.dto.readings;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReadingResponseDTO {

    private UUID id;
    private UUID deviceId;
    private Timestamp timestamp;
    private float readValue;
}