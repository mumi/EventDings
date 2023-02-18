package io.av360.eventdings.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.av360.eventdings.lib.dtos.SubscriptionDTO;

import java.util.List;

public class SubscriptionsResponse {
    public static List<SubscriptionDTO> fromJSON(String s) {
        ObjectMapper mapper = new ObjectMapper();
        List<SubscriptionDTO> subscriptions = null;
        try {
            subscriptions = mapper.readValue(s, mapper.getTypeFactory().constructCollectionType(List.class, SubscriptionDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
}
