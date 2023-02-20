package io.av360.eventdings.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.av360.eventdings.lib.grpc.Subscription;
import io.av360.eventdings.lib.grpc.SubscriptionId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.av360.eventdings.lib.dtos.SubscriptionDTO;

import java.util.*;


public final class SubscriptionManager {

    private static SubscriptionManager INSTANCE;

    private List<SubscriptionDTO> subscriptionDTOs = new ArrayList<SubscriptionDTO>();

    private SubscriptionManager() {
    }

    public static SubscriptionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SubscriptionManager();
        }

        return INSTANCE;
    }

    public SubscriptionId addSubscription(Subscription subscription) {
        if (subscriptionDTOs.stream().anyMatch(subscriptionDTO -> subscriptionDTO.getId().equals(UUID.fromString(subscription.getId())))) {
            deleteSubscription(SubscriptionId.newBuilder().setId(subscription.getId()).build());
        }

        subscriptionDTOs.add(
                new SubscriptionDTO(
                        UUID.fromString(subscription.getId()),
                        new Date(subscription.getCreatedAt().getSeconds() * 1000),
                        subscription.getSubscriberUri(),
                        subscription.getFiltersMap()
                )
        );

        return SubscriptionId.newBuilder().setId(subscription.getId()).build();
    }

    public SubscriptionId deleteSubscription(SubscriptionId subscriptionId) {
        subscriptionDTOs.removeIf(subscriptionDTO -> subscriptionDTO.getId().equals(UUID.fromString(subscriptionId.getId())));

        return subscriptionId;
    }

    public void deleteAllSubscriptions() {
        subscriptionDTOs.clear();
    }

    public List<SubscriptionDTO> findSubscriptions(String cloudevent) throws JsonProcessingException {
        List<SubscriptionDTO> matchingSubscriptions = new ArrayList<SubscriptionDTO>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(cloudevent);

        for (SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
            boolean allFiltersFound = true;
            for (Map.Entry<String, String> entry : subscriptionDTO.getFilters().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (!jsonNode.has(key) || !jsonNode.get(key).asText().equals(value)) {
                    allFiltersFound = false;
                    break;
                }
            }

            if (allFiltersFound) {
                matchingSubscriptions.add(subscriptionDTO);
            }
        }

        return matchingSubscriptions;
    }

    public boolean hasSubscriptions() {
        return !subscriptionDTOs.isEmpty();
    }
}
