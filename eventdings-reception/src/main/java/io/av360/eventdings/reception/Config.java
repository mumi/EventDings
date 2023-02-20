package io.av360.eventdings.reception;

public record Config(String host, String stream, String user, String password, String virtualHost, int port, int serverPort) {

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            String host = System.getenv("RABBITMQ_HOST");
            String stream = System.getenv("RABBITMQ_STREAM");
            String user = System.getenv("RABBITMQ_USER");
            String password = System.getenv("RABBITMQ_PASSWORD");
            String virtualHost = System.getenv("RABBITMQ_VHOST");
            String port = System.getenv("RABBITMQ_STREAM_PORT");
            String serverPort = System.getenv("SERVER_PORT");

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

            if (port == null || port.isEmpty()) {
                port = "5552";
            }

            if (serverPort == null || serverPort.isEmpty()) {
                serverPort = "80";
            }

            instance = new Config(host, stream, user, password, virtualHost, Integer.parseInt(port), Integer.parseInt(serverPort));
        }
        return instance;
    }


}

