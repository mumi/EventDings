package org.av360.maverick.eventdispatcher.subscriptions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
@OpenAPIDefinition
//        (
//        info = @Info(
//                title = "EventDings Subscribing API",
//                version = "1.0",
//                description = "API for subscribing to events"
//        )
//)
public class OpenApiConfiguration {
    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/"), req ->
                ServerResponse.temporaryRedirect(URI.create("/swagger-ui.html")).build()
        );
    }
    @Bean
    public GroupedOpenApi subscribingAPI(@Value("${info.app.version:unknown}") String version) {
        return GroupedOpenApi.builder()
                .group("Subscription API")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("Subscription API").description("API for subscribing to events").version(version));
                })
                .pathsToMatch("/subscriptions/**")
                .build();
    }

    @Bean
    public GroupedOpenApi grpcAPI(@Value("${info.app.version:unknown}") String version) {
        return GroupedOpenApi.builder()
                .group("gRPC API")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("gRPC Webhook").description("Webhook for the dispatcher to start subscription stream call").version(version));
                })
                .pathsToMatch("/grpc")
                .build();
    }
}
