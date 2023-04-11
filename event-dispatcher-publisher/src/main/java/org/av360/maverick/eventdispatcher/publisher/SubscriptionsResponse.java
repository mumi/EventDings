package org.av360.maverick.eventdispatcher.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.av360.maverick.eventdispatcher.shared.domain.Subscription;
import org.av360.maverick.eventdispatcher.shared.dto.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionsResponse {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionsResponse.class);
    public static List<Subscription> fromJSON(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            List<ObjectNode> jsonNodes = objectMapper.readValue(s, objectMapper.getTypeFactory().constructCollectionType(List.class, ObjectNode.class));

            for (ObjectNode jsonNode : jsonNodes) {
                String addressable = jsonNode.get("addressable").asText();
//                Map<String, String> filters = new HashMap<>();
//
//                if (jsonNode.has("filters")) {
//                    ObjectNode filtersNode = (ObjectNode) jsonNode.get("filters");
//                    filtersNode.fields().forEachRemaining(entry -> filters.put(entry.getKey(), entry.getValue().asText()));
//                }

                Subscription subscription = new Subscription();
                subscription.setSubscriberUri(addressable);
                subscription.setId(jsonNode.get("identifier").asLong());
                subscription.setCreatedAt(LocalDateTime.parse(jsonNode.get("creationDate").asText(), DateTimeFormatter.ISO_DATE_TIME));

                subscriptions.add(subscription);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return subscriptions;
    }
}
