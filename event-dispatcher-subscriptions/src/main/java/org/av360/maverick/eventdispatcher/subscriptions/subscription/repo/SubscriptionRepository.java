package org.av360.maverick.eventdispatcher.subscriptions.subscription.repo;

import org.av360.maverick.eventdispatcher.shared.domain.Subscription;
import org.springframework.data.r2dbc.repository.R2dbcRepository;


public interface SubscriptionRepository extends R2dbcRepository<Subscription, Long> {
}

