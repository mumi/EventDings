package io.av360.eventdings.dispatcher;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class SubscriptionDTO {
    private UUID id;
    private Date createdAt;
    private String subscriberUri;
    private Map<String, String> filters;
}