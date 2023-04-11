package org.av360.maverick.eventdispatcher.shared.domain;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.signature.qual.Identifier;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    @Identifier
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

