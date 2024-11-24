package ro.tucn.energy_mgmt_monitoring_comm.dto.chartData;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartDataResponseDTO {
    private int hour;
    private float readValue;
}
