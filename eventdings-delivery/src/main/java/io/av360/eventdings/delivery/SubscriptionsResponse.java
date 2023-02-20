package io.av360.eventdings.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.av360.eventdings.lib.dtos.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SubscriptionsResponse {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionsResponse.class);
    public static List<SubscriptionDTO> fromJSON(String s) {
        ObjectMapper mapper = new ObjectMapper();
        List<SubscriptionDTO> subscriptions = null;
        try {
            subscriptions = mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, SubscriptionDTO.class));
        } catch (Exception e) {
            log.error("Error parsing subscriptions response", e);
        }
        return subscriptions;
    }
}
