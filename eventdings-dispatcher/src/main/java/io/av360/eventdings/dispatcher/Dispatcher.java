package io.av360.eventdings.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.av360.eventdings.dispatcher.rabbit.RabbitMQClassic;
import io.av360.eventdings.lib.dtos.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    public static void dispatch(String cloudevent) {
        List<SubscriptionDTO> foundSubscriptions = null;

        try {
            foundSubscriptions = SubscriptionManager.getInstance().findSubscriptions(cloudevent);
        } catch (JsonProcessingException e) {
            log.error("Error parsing CloudEvent", e);
        }

        RabbitMQClassic rabbitMQClassic = RabbitMQClassic.getInstance();

        assert foundSubscriptions != null;
        for (SubscriptionDTO subscription : foundSubscriptions) {
            boolean success;

            do {
                success = rabbitMQClassic.publish("sub_" + subscription.getId(), cloudevent);
                if (!success) {
                    log.error("Error publishing CloudEvent to subscription " + subscription.getId());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } while (!success);
        }
    }
}
