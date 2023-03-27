package org.av360.maverick.eventdispatcher.subscriptions.subscription.api;

import org.av360.maverick.eventdispatcher.shared.dto.SubscriptionDTO;
import org.av360.maverick.eventdispatcher.subscriptions.subscription.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/grpc")
    public ResponseEntity getGrpc() {
        subscriptionService.getGrpc();

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/subscriptions")
    public Mono<ResponseEntity<SubscriptionDTO.Response>> createSubscription(@RequestBody SubscriptionDTO.Request request, UriComponentsBuilder componentsBuilder) {
        return subscriptionService
                .createSubscription(request)
                .map(SubscriptionDTO.Response::from)
                .map(response -> {
                    URI uri = componentsBuilder.path("{id}").buildAndExpand(response.identifier()).toUri();
                    return ResponseEntity.created(uri).body(response);
                });

    }

    @GetMapping("/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SubscriptionDTO.ShortResponse> getAllSubscriptions() {
        return subscriptionService
                .getAllSubscriptions()
                .map(SubscriptionDTO.ShortResponse::from);
    }

    @GetMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SubscriptionDTO.Response> getSubscription(@PathVariable Long identifier) {
        return subscriptionService.getSubscription(identifier)
                .map(SubscriptionDTO.Response::from);
    }

    @DeleteMapping("/subscriptions/{id}")
    public Mono<Void> deleteSubscription(@PathVariable Long id) {
        return subscriptionService.deleteSubscription(id);
    }
}
