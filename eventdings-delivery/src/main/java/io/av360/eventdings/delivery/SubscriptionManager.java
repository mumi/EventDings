package io.av360.eventdings.delivery;

import io.av360.eventdings.lib.dtos.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SubscriptionManager {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionManager.class);
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
        if (hasSubscription(subscriptionId)) {
            removeSubscription(subscriptionId);
        }

        if (consumerManager.addConsumer(subscriptionId)) {
            subscriptions.put(subscriptionId, subscriberUrl);
        } else {
            log.error("Error adding subscription " + subscriptionId);
        }
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
        consumerManager.removeConsumer(subscriptionId);
        subscriptions.remove(subscriptionId);
    }

    public boolean hasSubscription(UUID subscriptionId) {
        return subscriptions.containsKey(subscriptionId);
    }
}
