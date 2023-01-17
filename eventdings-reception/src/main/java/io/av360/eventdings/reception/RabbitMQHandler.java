package io.av360.eventdings.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQHandler {

    private static RabbitMQHandler instance;
    private static Connection connection;
    private static Channel channel;

    private static final Logger log = LoggerFactory.getLogger(RabbitMQHandler.class);
    private RabbitMQHandler() {
    }


    public static RabbitMQHandler getInstance() {
        if (instance == null) {
            instance = new RabbitMQHandler();
        }
        return instance;
    }

    public void init() throws IOException, NoSuchAlgorithmException, URISyntaxException, KeyManagementException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(Config.getInstance().AMQP_URI);
        connection = factory.newConnection();
        channel = connection.createChannel();

        Map<String, Object> args = Map.of(
                "x-queue-type", "stream",
                "x-max-age", "86400s",
                "x-stream-max-segment-size-bytes", 500000000,
                "x-queue-leader-locator", "least-leaders"
        );

        channel.exchangeDeclare(Config.getInstance().AMQP_ROUTING_KEY, "direct", true);
        channel.queueDeclare(Config.getInstance().AMQP_QUEUE, true, false, false, args);
        channel.queueBind(Config.getInstance().AMQP_QUEUE, Config.getInstance().AMQP_EXCHANGE, Config.getInstance().AMQP_ROUTING_KEY);
    }

    public static boolean publish(String message) {
        try {
            channel.basicPublish(Config.getInstance().AMQP_EXCHANGE, Config.getInstance().AMQP_ROUTING_KEY, null, message.getBytes());
            return true;
        } catch (Exception e) {
            log.error("Error while publishing message to RabbitMQ", e);
            return false;
        }
    }

    public static void close() throws Exception {
        channel.close();
        connection.close();
    }
}