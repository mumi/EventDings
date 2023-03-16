package org.av360.maverick.eventdispatcher.subscriptions.subscription.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}

