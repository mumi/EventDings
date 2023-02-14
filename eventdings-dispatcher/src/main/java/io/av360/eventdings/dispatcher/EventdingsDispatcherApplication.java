package io.av360.eventdings.dispatcher;

import io.av360.eventdings.dispatcher.rabbit.RabbitMQClassic;
import io.av360.eventdings.dispatcher.rabbit.RabbitMQStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@SpringBootApplication
public class EventdingsDispatcherApplication {
    private static final Logger log = LoggerFactory.getLogger(EventdingsDispatcherApplication.class);
    private static Config cfg = Config.getInstance();

    public static void main(String[] args) {
        SpringApplication.run(EventdingsDispatcherApplication.class, args);


        //TODO: This is a hack to wait for the gRPC server to start
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //TODO: Refactoren & Auslagern
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(URI.create(cfg.subscribingUrl() + "/grpc")).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Error getting gRPC request", e);
        }

        RabbitMQClassic.getInstance().init();
        RabbitMQStream.getInstance().init();

    }

}
