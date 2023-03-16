package org.av360.maverick.eventdispatcher.shared.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class SubscriptionDTO {

    private UUID id;
    private Date createdAt;
    private String subscriberUri;
    private Map<String, String> filters;
}
