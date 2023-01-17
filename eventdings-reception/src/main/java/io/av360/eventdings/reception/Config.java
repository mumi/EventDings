package io.av360.eventdings.reception;

public class Config {
    String AMQP_URI;
    String AMQP_EXCHANGE;
    String AMQP_QUEUE;
    String AMQP_ROUTING_KEY;

    private Config() {

        AMQP_URI = System.getenv("RABBITMQ_URL");
        AMQP_EXCHANGE = System.getenv("RABBITMQ_EXCHANGE");
        AMQP_QUEUE = System.getenv("RABBITMQ_QUEUE");
        AMQP_ROUTING_KEY = System.getenv("RABBITMQ_ROUTING_KEY");

        if (AMQP_URI == null || AMQP_URI.isEmpty()) {
            throw new IllegalArgumentException("RABBITMQ_URL is not set");
        }

        if (AMQP_EXCHANGE == null || AMQP_EXCHANGE.isEmpty()) {
            throw new IllegalArgumentException("RABBITMQ_EXCHANGE is not set");
        }

        if (AMQP_QUEUE == null || AMQP_QUEUE.isEmpty()) {
            throw new IllegalArgumentException("RABBITMQ_QUEUE is not set");
        }

        if (AMQP_ROUTING_KEY == null || AMQP_ROUTING_KEY.isEmpty()) {
            throw new IllegalArgumentException("RABBITMQ_ROUTING_KEY is not set");
        }
    }

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
