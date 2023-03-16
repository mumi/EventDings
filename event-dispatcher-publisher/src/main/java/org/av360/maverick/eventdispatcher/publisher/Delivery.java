package io.av360.eventdings.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

public class Delivery {
    private static final Logger log = LoggerFactory.getLogger(Delivery.class);

    static HttpClient client = HttpClient.newHttpClient();
    public static Boolean deliver(UUID subscriptionId, String cloudevent) {
        String url = SubscriptionManager.getInstance().getSubscriberUrl(subscriptionId);

        if (url == null) {
            log.info("No subscription for " + subscriptionId);
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cloudevent))
                .build();

        Integer statusCode = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .join();


        if (statusCode / 100 == 2) {
            log.debug("Delivered to " + url + " with status code " + statusCode + " for subscription " + subscriptionId);
            return true;
        } else {
            log.error("Error delivering to " + url + " with status code " + statusCode + " for subscription " + subscriptionId);
            return false;
        }
    }
}
