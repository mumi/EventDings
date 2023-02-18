package io.av360.eventdings.delivery;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConsumerManager {

    private static final Logger log = LoggerFactory.getLogger(ConsumerManager.class);
    private static ConsumerManager instance = null;
    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private HashMap<UUID, Consumer> consumers = new HashMap<>();
    private final Delivery delivery = new Delivery();

    private ConsumerManager() {
        Config cfg = Config.getInstance();

        connectionFactory.setHost(cfg.host());
        connectionFactory.setPort(cfg.amqpPort());
        connectionFactory.setUsername(cfg.user());
        connectionFactory.setPassword(cfg.password());
        connectionFactory.setVirtualHost(cfg.virtualHost());

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            log.error("Error creating connection", e);
        }
    }

    public static ConsumerManager getInstance() {
        if (instance == null) {
            instance = new ConsumerManager();
        }
        return instance;
    }

    private Consumer createConsumer(UUID subscriptionId) {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                log.debug("Received '" + message + "'");

                boolean success = delivery.deliver(subscriptionId, message);

                if (success) {
                    log.debug("Acking message");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } else {
                    log.debug("Nacking message");
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        };

        try {
            channel.basicConsume("sub_" + subscriptionId, false, consumer);
        } catch (IOException e) {
            log.error("Error creating consumer", e);
        }

        return consumer;
    }

    public void addConsumer(UUID subscriptionId) {
        consumers.put(subscriptionId, createConsumer(subscriptionId));
    }

    public void removeConsumer(UUID subscriptionId) {
        try {
            channel.basicCancel( "sub_" + subscriptionId);
        } catch (IOException e) {
            log.error("Error removing consumer", e);
        }

        consumers.remove(subscriptionId);
    }
}
