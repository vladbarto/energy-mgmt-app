package ro.tucn.energy_mgmt_devices.ampq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import ro.tucn.energy_mgmt_devices.dto.deviceChange.SendingStatus;

public interface MessageSender<Request> {

    Queue queue();
    TopicExchange exchange();
    Binding binding(Queue queue, TopicExchange exchange);
    SendingStatus sendMessage(Request request);
}