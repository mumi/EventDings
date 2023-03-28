package org.av360.maverick.eventdispatcher.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.av360.maverick.eventdispatcher.filter.rabbit.RabbitMQClassic;
import org.av360.maverick.eventdispatcher.shared.domain.Subscription;
import org.av360.maverick.eventdispatcher.shared.dto.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    public static void dispatch(String cloudevent) {
        List<Subscription> foundSubscriptions = null;

        try {
            foundSubscriptions = SubscriptionManager.getInstance().findSubscriptions(cloudevent);
        } catch (JsonProcessingException e) {
            log.error("Error parsing CloudEvent", e);
        }

        RabbitMQClassic rabbitMQClassic = RabbitMQClassic.getInstance();

        assert foundSubscriptions != null;
        for (Subscription subscription : foundSubscriptions) {
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
