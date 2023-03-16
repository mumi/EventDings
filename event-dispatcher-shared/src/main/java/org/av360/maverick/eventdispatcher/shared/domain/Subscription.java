package org.av360.maverick.eventdispatcher.shared.domain;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    private Long id;
    private LocalDateTime createdAt;
    private String subscriberUri;

    private Map<String, String> filters;


    public Subscription(String addressable, Map<String, String> filters) {
        this.subscriberUri = addressable;
        this.filters = filters;
        this.createdAt = LocalDateTime.now();
    }
}

