package io.av360.eventdings.dispatcher.rabbit;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import io.av360.eventdings.dispatcher.Config;
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
        log.debug("RabbitMQClassic.init");
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
            log.error("Error connecting to RabbitMQ");
            throw e;
        }
    }

    public boolean publish(String queue, String message){
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
}
