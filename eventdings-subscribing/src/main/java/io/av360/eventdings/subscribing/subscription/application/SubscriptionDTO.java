package io.av360.EventDings.Subscribing.subscription.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionDTO {

    private UUID id;
    private Date createdAt;
    private String subscriberUri;
    private Map<String, String> filter;
}
