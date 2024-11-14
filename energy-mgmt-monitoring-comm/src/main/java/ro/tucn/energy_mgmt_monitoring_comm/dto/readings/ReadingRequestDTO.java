package ro.tucn.energy_mgmt_monitoring_comm.dto.readings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingRequestDTO {

    private UUID deviceId;
    private Timestamp timestamp;
    private float readValue;
}
