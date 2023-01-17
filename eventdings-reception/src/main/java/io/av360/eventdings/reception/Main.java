package io.av360.eventdings.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, URISyntaxException, KeyManagementException, TimeoutException {
        Logger log = LoggerFactory.getLogger(Main.class);

        log.info("Starting application");

        log.info("Initializing RabbitMQ connection");
        RabbitMQHandler.getInstance().init();
        log.info("RabbitMQ connection initialized");

        log.info("Starting Webserver");
        WebhookApi.start(80, "/webhook");
        log.info("Webserver started");
    }
}