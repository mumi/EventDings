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
    private ConsumerManager() {
        Config cfg = Config.getInstance();

        connectionFactory.setHost(cfg.host());
        connectionFactory.setPort(cfg.amqpPort());
        connectionFactory.setUsername(cfg.user());
        connectionFactory.setPassword(cfg.password());
        connectionFactory.setVirtualHost(cfg.virtualHost());

        log.info("Initializing RabbitMQ connection");
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            log.info("RabbitMQ connection initialized");
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

                if (Delivery.deliver(subscriptionId, message)) {
                    log.debug("Acking message " + envelope.getDeliveryTag());
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } else {
                    log.debug("Nacking message " + envelope.getDeliveryTag());
                    channel.basicNack(envelope.getDeliveryTag(), false, true);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        try {
            channel.basicConsume("sub_" + subscriptionId, false, consumer);
        } catch (IOException e) {
            log.error("Error creating consumer", e);
            return null;
        }

        return consumer;
    }

    public boolean addConsumer(UUID subscriptionId) {
        if (consumers.containsKey(subscriptionId)) {
            log.info("Consumer already exists. Readding anyway.");
            removeConsumer(subscriptionId);
        }

        Consumer consumer = createConsumer(subscriptionId);

        if (consumer == null) {
            return false;
        }

        consumers.put(subscriptionId, consumer);
        return true;
    }

    public void removeConsumer(UUID subscriptionId) {
        try {
            channel.basicCancel( "sub_" + subscriptionId);
        } catch (IOException e) {
            log.error("Error cancelling consumer. Removing anyway.", e);
        }

        consumers.remove(subscriptionId);
    }
}
