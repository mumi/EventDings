package io.av360.eventdings.delivery;

import io.av360.eventdings.lib.dtos.SubscriptionDTO;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SubscriptionManager {
    private static SubscriptionManager instance = null;
    private HashMap<UUID, String> subscriptions = new HashMap<>();
    private ConsumerManager consumerManager;

    private SubscriptionManager() {
        consumerManager = ConsumerManager.getInstance();
    }

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            instance = new SubscriptionManager();
        }
        return instance;
    }

    public void addSubscription(UUID subscriptionId, String subscriberUrl) {
        subscriptions.put(subscriptionId, subscriberUrl);
        consumerManager.addConsumer(subscriptionId);
    }

    public String getSubscriberUrl(UUID subscriptionId) {
        return subscriptions.get(subscriptionId);
    }

    public void refreshSubscriptions(List<SubscriptionDTO> subscriptions) {
        for (SubscriptionDTO subscription : subscriptions) {
            if (!hasSubscription(subscription.getId())) {
                addSubscription(subscription.getId(), subscription.getSubscriberUri());
            }
        }

        for (UUID subscriptionId : this.subscriptions.keySet()) {
            if (subscriptions.stream().noneMatch(s -> s.getId().equals(subscriptionId))) {
                removeSubscription(subscriptionId);
            }
        }
    }

    public void removeSubscription(UUID subscriptionId) {
        subscriptions.remove(subscriptionId);
        consumerManager.removeConsumer(subscriptionId);
    }

    public boolean hasSubscription(UUID subscriptionId) {
        return subscriptions.containsKey(subscriptionId);
    }
}
