package io.av360.eventdings.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;
import io.av360.eventdings.lib.CloudEventValidator;

public class WebhookApi {
    private static final Logger log = LoggerFactory.getLogger(WebhookApi.class);

    public static void start(Integer port, String path) {
        HttpServer.create()
                .port(port)
                .route(routes -> routes.post(path,
                        (request, response) -> {

                            Mono<String> requestBody = request.receive().retain().aggregate().asString();

                            return requestBody.filter(CloudEventValidator::isValidCloudEvent)
                                    .switchIfEmpty(Mono.error(new IllegalArgumentException("Invalid format for CloudEvent")))
                                    .flatMap(event -> RabbitMQHandler.getInstance().publish(event))
                                    .thenEmpty(response.status(202));
                        })).bindNow();
}
}