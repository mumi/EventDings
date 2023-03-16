package org.av360.maverick.eventdispatcher.publisher;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class ConsumerManager {

    private static final Logger log = LoggerFactory.getLogger(ConsumerManager.class);
    private static ConsumerManager instance = null;
    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private Connection connection;
    private HashMap<UUID, DefaultConsumer> consumers = new HashMap<>();
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

    private DefaultConsumer _createConsumer(UUID subscriptionId) {
        Channel channel;

        try {
            channel = connection.createChannel();
            log.info("Created channel " + channel.getChannelNumber());
        } catch (IOException e) {
            log.error("Error creating channel", e);
            return null;
        }

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                log.debug("Received '" + message + "'");

                Boolean deliverySuccessful = Delivery.deliver(subscriptionId, message);

                if (Boolean.TRUE.equals(deliverySuccessful)) {
                    log.debug("Acking message " + envelope.getDeliveryTag());
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } else if (Boolean.FALSE.equals(deliverySuccessful)) {
                    log.debug("Nacking message " + envelope.getDeliveryTag());
                    channel.basicNack(envelope.getDeliveryTag(), false, true);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                } else {
                    log.info("Cancelling consumer " + consumerTag + " and closing channel " + channel.getChannelNumber());

                    try {
                        channel.basicCancel(consumerTag);
                        channel.close();
                    } catch (TimeoutException e) {
                        log.debug("Error closing channel", e);
                    }
                }
            }
        };

        try {
            channel.basicConsume("sub_" + subscriptionId, false, consumer);
        } catch (IOException e) {
            log.error("Error creating consumer for subscription", e);
            return null;
        }

        return consumer;
    }

    public DefaultConsumer createConsumer(UUID subscriptionId) {
        if (consumers.containsKey(subscriptionId)) {
            log.info("Consumer already exists. Readding anyway.");
            removeConsumer(subscriptionId);
        }

        DefaultConsumer consumer = _createConsumer(subscriptionId);

        if (consumer == null) {
            return null;
        }

        consumers.put(subscriptionId, consumer);
        return consumer;
    }

    public void removeConsumer(UUID subscriptionId) {
        String consumerTag = consumers.get(subscriptionId).getConsumerTag();
        Channel channel = consumers.get(subscriptionId).getChannel();

        log.info("Subscription deleted. Cancelling consumer " + consumerTag + " and closing channel " + channel.getChannelNumber());
        try {
            channel.basicCancel(consumerTag);
            channel.close();
        } catch (IOException | TimeoutException e) {
            log.debug("Error cancelling consumer / closing channel", e);
        }

        consumers.remove(subscriptionId);
    }
}
