package io.av360.eventdings.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;
import static io.av360.eventdings.reception.CloudEventValidator.isValidCloudEvent;

public class WebhookApi {
    private static final Logger log = LoggerFactory.getLogger(WebhookApi.class);
    public static void start(Integer port, String path) {
        HttpServer.create()
                .port(port)
                .route(routes -> routes.post(path, (request, response) -> {

                    Mono<String> requestBody = request.receive().retain().aggregate().asString();
                    return requestBody.flatMap(body -> {

                        if (!isValidCloudEvent(body)) {
                            log.error("Invalid CloudEvent received");

                            response.status(400);
                            response.sendString(Mono.just("Invalid CloudEvent"));
                            return response.then();
                        }

                        if (RabbitMQHandler.publish(body)) {
                            response.status(202);
                        } else {
                            response.status(500);
                            response.sendString(Mono.just("Error forwarding CloudEvent to RabbitMQ Stream"));
                        }

                        return response.then();
                    });
                })).bindNow();
    }
}