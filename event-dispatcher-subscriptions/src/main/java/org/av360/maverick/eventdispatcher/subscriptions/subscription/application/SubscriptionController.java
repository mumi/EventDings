package io.av360.eventdings.subscribing.subscription.application;

import io.av360.eventdings.lib.dtos.SubscriptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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
    public ResponseEntity createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        SubscriptionDTO processedDTO = subscriptionService.createSubscription(subscriptionDTO);
        URI returnURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(processedDTO.getId())
                .toUri();
        return ResponseEntity
                .created(returnURI)
                .body(processedDTO);

    }

    @GetMapping("/subscriptions")
    public ResponseEntity getAllSubscriptions() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/subscriptions/{id}")
    public ResponseEntity getSubscription(@PathVariable UUID id) {
        SubscriptionDTO subscriptionDTO = subscriptionService.getSubscription(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subscriptionDTO);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity deleteSubscription(@PathVariable UUID id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
