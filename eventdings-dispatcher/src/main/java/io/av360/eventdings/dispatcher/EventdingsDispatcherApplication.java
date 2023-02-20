package io.av360.eventdings.dispatcher;

import io.av360.eventdings.dispatcher.rabbit.RabbitMQClassic;
import io.av360.eventdings.dispatcher.rabbit.RabbitMQStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.TimeoutException;


@SpringBootApplication
public class EventdingsDispatcherApplication {
    public static void main(String[] args) throws IOException, TimeoutException {
        Logger log = LoggerFactory.getLogger(EventdingsDispatcherApplication.class);

        SpringApplication.run(EventdingsDispatcherApplication.class, args);

        log.info("Starting Grpc Subscription Refresh Task");
        Timer timer = new Timer();
        timer.schedule(new RefreshGrpcSubscriptionsTask(), 0, 5 * 60 * 1000);

        log.info("Initializing RabbitMQ AMQP connection");
        RabbitMQClassic.getInstance().init();
        log.info("RabbitMQ AMQP connection initialized");

        log.info("Initializing RabbitMQ Stream connection");
        RabbitMQStream.getInstance().init();
        log.info("RabbitMQ Stream connection initialized");
    }

}
