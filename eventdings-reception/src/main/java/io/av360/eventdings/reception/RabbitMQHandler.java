package io.av360.eventdings.reception;

import com.rabbitmq.stream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
public class RabbitMQHandler {

    private static RabbitMQHandler instance;

    Environment environment;

    private static final Logger log = LoggerFactory.getLogger(RabbitMQHandler.class);
    private Producer producer;

    private RabbitMQHandler() {
    }


    public static RabbitMQHandler getInstance() {
        if (instance == null) {
            instance = new RabbitMQHandler();
            instance.init();
        }
        return instance;
    }

    public void init()  {
        Config cfg = Config.getInstance();

        environment = Environment.builder()
                .host(cfg.host())
                .username(cfg.user())
                .password(cfg.password())
                .port(cfg.port())
                .virtualHost(cfg.virtualHost())
                .build();

//        environment.streamCreator()
//                .maxAge(Duration.ofDays(1))
//                .maxLengthBytes(ByteCapacity.GB(10))
//                .maxSegmentSizeBytes(ByteCapacity.MB(50))
//                .stream(cfg.stream())
//                .create();

        this.producer = this.environment.producerBuilder().stream(Config.getInstance().stream()).build();
    }

    public Mono<Boolean> publish(String message) {
        return Mono.create(sink -> {
            MessageBuilder messageBuilder = this.producer.messageBuilder();
            messageBuilder.addData(message.getBytes());

            ConfirmationHandler callback = status -> {
                if (status.isConfirmed()) {
                    sink.success();
                } else {
                    log.error("Error while publishing message to RabbitMQ");
                    sink.error(new IOException("Failed to publish message to stream with message"+status.getCode()));
                }
            };
        });
    }

    public void close() throws Exception {
        this.producer.close();
        this.environment.close();
    }
}