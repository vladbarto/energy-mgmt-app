package ro.tucn.energy_mgmt_devices.ampq;

import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;

public interface MessageSender<Request> {

    SendingStatus sendMessage(Request request);
}