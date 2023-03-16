package org.av360.maverick.eventdispatcher.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.TimerTask;

public class RefreshGrpcSubscriptionsTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(RefreshGrpcSubscriptionsTask.class);
    Config config = Config.getInstance();
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder(URI.create(config.subscribingUrl() + "/grpc")).build();

    public void run() {
        log.debug("Refreshing Grpc Subscriptions");

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Error reaching Subscription API to refresh Subscriptions", e);
        }
    }
}
