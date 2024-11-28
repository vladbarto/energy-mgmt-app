package ro.tucn.energy_mgmt_devices.util;

import lombok.experimental.UtilityClass;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeRequestDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.DeviceChangeResponseDTO;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;

@UtilityClass
public class MessageUtils {

    public DeviceChangeResponseDTO getDeviceChangeResponseDTO(DeviceChangeRequestDTO deviceChangeRequestDTO, SendingStatus status) {
        return DeviceChangeResponseDTO.builder()
                .deviceId(deviceChangeRequestDTO.getDeviceId())
                .userId(deviceChangeRequestDTO.getUserId())
                .status(status)
                .build();
    }

}
