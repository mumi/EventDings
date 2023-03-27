package org.av360.maverick.eventdispatcher.filter;

public record Config(String host, String stream, String user, String password, String virtualHost, int streamPort, int amqpPort , String subscribingUrl) {

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            String host = System.getenv("RABBITMQ_HOST");
            String stream = System.getenv("RABBITMQ_STREAM");
            String user = System.getenv("RABBITMQ_USER");
            String password = System.getenv("RABBITMQ_PASSWORD");
            String virtualHost = System.getenv("RABBITMQ_VHOST");
            String streamPort = System.getenv("RABBITMQ_STREAM_PORT");
            String amqpPort = System.getenv("RABBITMQ_AMQP_PORT");
            String subscribingUrl = System.getenv("SUBSCRIBING_URL");


            if (host == null || host.isEmpty()) {
                throw new IllegalArgumentException("RABBITMQ_HOST is not set");
            }

            if (stream == null || stream.isEmpty()) {
                throw new IllegalArgumentException("RABBITMQ_STREAM is not set");
            }

            if (user == null || user.isEmpty()) {
                throw new IllegalArgumentException("RABBITMQ_USER is not set");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("RABBITMQ_PASSWORD is not set");
            }

            if (virtualHost == null || virtualHost.isEmpty()) {
                virtualHost = "/";
            }

            if (streamPort == null || streamPort.isEmpty()) {
                streamPort = "5552";
            }

            if (amqpPort == null || amqpPort.isEmpty()) {
                amqpPort = "5672";
            }

            if (subscribingUrl == null || subscribingUrl.isEmpty()) {
                throw new IllegalArgumentException("SUBSCRIBING_URL is not set");
            }

            instance = new Config(host, stream, user, password, virtualHost, Integer.parseInt(streamPort), Integer.parseInt(amqpPort) , subscribingUrl);
        }
        return instance;
    }


}

