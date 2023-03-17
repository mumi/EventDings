package org.av360.maverick.eventdispatcher.subscriptions.subscription.services;

import org.av360.maverick.eventdispatcher.shared.dto.SubscriptionDTO;
import org.av360.maverick.eventdispatcher.subscriptions.grpc.GrpcClientService;
import org.av360.maverick.eventdispatcher.shared.domain.Subscription;
import org.av360.maverick.eventdispatcher.subscriptions.subscription.repo.SubscriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subRepo;

    @Autowired
    private GrpcClientService grpcClientService;

    public Object getGrpc() {
        grpcClientService.sendSubscriptions(getAllSubscriptions());
        return null;
    }

    public Mono<Subscription> createSubscription(SubscriptionDTO.Request requestedSubscription) {

        Subscription sub = new Subscription(
                requestedSubscription.addressable(),
                requestedSubscription.filters()
        );
        return subRepo.save(sub)
                .doOnSuccess(savedSubscription -> this.grpcClientService.sendSubscription(savedSubscription).subscribe());
    }

    public Flux<Subscription> getAllSubscriptions() {
        return subRepo.findAll();
    }

    public Mono<Subscription> getSubscription(Long id) {
        return subRepo.findById(id);
    }

    public Mono<Void> deleteSubscription(Long id) {
        return subRepo.deleteById(id)
                .doOnSuccess(onSuccess -> this.grpcClientService.deleteSubscription(id).subscribe());
    }


}
