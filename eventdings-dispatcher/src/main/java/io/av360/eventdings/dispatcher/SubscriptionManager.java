package io.av360.eventdings.dispatcher;

import io.av360.eventdings.grpc.Subscription;
import io.av360.eventdings.grpc.SubscriptionId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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
}
