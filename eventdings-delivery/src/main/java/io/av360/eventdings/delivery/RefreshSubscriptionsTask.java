package io.av360.eventdings.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.TimerTask;

public class RefreshSubscriptionsTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(RefreshSubscriptionsTask.class);
    Config config = Config.getInstance();
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(config.subscribingUrl() + "/subscriptions"))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .GET()
            .build();
    public void run() {
        log.debug("Refreshing subscriptions");
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(SubscriptionsResponse::fromJSON)
                .thenAccept(SubscriptionManager.getInstance()::refreshSubscriptions)
                .join();
    }
}
