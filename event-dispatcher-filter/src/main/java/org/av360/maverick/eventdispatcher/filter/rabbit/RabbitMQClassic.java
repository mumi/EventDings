package org.av360.maverick.eventdispatcher.filter.rabbit;

import com.rabbitmq.client.*;
import org.av360.maverick.eventdispatcher.filter.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQClassic {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQClassic.class);
    private static RabbitMQClassic instance;

    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private Connection connection;

    private Channel channel;
    private RabbitMQClassic() {
    }

    public static RabbitMQClassic getInstance() {
        if (instance == null) {
            instance = new RabbitMQClassic();
        }
        return instance;
    }

    public void init() throws IOException, TimeoutException {
        log.info("Initializing RabbitMQ AMQP connection");
        Config cfg = Config.getInstance();

        connectionFactory.setHost(cfg.host());
        connectionFactory.setPort(cfg.amqpPort());
        connectionFactory.setUsername(cfg.user());
        connectionFactory.setPassword(cfg.password());
        connectionFactory.setVirtualHost(cfg.virtualHost());

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            log.info("RabbitMQ AMQP connection initialized");
        } catch (IOException | TimeoutException e) {
            log.error("Error connecting to RabbitMQ");
            throw e;
        }
    }

    public boolean publish(String queue, String message){
        if (!channel.isOpen()) {
            log.info("Channel is not open. Creating new channel");
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                log.error("Error creating channel", e);
            }
        }

        try {
            channel.queueDeclare(queue, true, false, false, null);
        } catch (IOException e) {
            log.debug("Queue already exists", e);
        }

        try {
            channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        } catch (Exception e) {
            log.error("Error publishing message", e);
            return false;
        }

        return true;
    }

    public void createQueue(String queue) {
        if (!channel.isOpen()) {
            log.info("Channel is not open. Creating new channel");
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                log.error("Error creating channel", e);
            }
        }

        try {
            channel.queueDeclare(queue, true, false, false, null);
        } catch (Exception e) {
            log.error("Error creating queue", e);
        }

        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            log.error("Error closing channel", e);
        }
    }

    public void deleteQueue(String queue) {
        if (!channel.isOpen()) {
            log.info("Channel is not open. Creating new channel");
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                log.error("Error creating channel", e);
            }
        }

        try {
            channel.queueDelete(queue);
        } catch (Exception e) {
            log.error("Error deleting queue", e);
        }
    }
}
