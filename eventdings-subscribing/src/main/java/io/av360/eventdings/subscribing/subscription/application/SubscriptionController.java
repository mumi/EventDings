package io.av360.EventDings.Subscribing.subscription.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
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
    public ResponseEntity deleteSubscription(@RequestBody UUID id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
