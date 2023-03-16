package org.av360.maverick.eventdispatcher.subscriptions.subscription.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id
    private UUID id;
    private Date createdAt;
    private String subscriberUri;

    @ElementCollection
    @MapKeyColumn(name = "filter_key")
    @Column(name = "filter_value")
    @CollectionTable(name = "subscription_filters", joinColumns = @JoinColumn(name = "subscription_id"))
    private Map<String, String> filters;

    public Subscription(UUID id, Date createdAt, Map<String, String> filter, String subscriberUri) {
        this.id = id;
        this.createdAt = createdAt;
        this.filters = filter;
        this.subscriberUri = subscriberUri;
    }
}

