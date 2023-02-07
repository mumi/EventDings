package io.av360.eventdings.subscribing;

import com.google.protobuf.Timestamp;
import io.av360.eventdings.grpc.*;
import io.av360.eventdings.subscribing.subscription.application.SubscriptionDTO;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class GrpcClientService {
    Logger log = LoggerFactory.getLogger(GrpcClientService.class);
    @GrpcClient("eventdings-dispatcher")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionServiceBlockingStub;

    @GrpcClient("eventdings-dispatcher")
    private SubscriptionServiceGrpc.SubscriptionServiceStub subscriptionServiceStub;

    private Subscription subscriptionDTOToGrpcSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription.Builder subBuilder = Subscription.newBuilder();
        subBuilder.setId(subscriptionDTO.getId().toString());
        subBuilder.setCreatedAt(Timestamp.newBuilder().setSeconds(subscriptionDTO.getCreatedAt().getTime() / 1000).build());
        subBuilder.setSubscriberUri(subscriptionDTO.getSubscriberUri());
        subBuilder.putAllFilters(subscriptionDTO.getFilters());
        return subBuilder.build();
    }

    //TODO: send subscriptions to dispatcher as strean
    public void sendSubscriptions(ArrayList<SubscriptionDTO> subscriptionDTOs) {

    }


    public void sendSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionDTOToGrpcSubscription(subscriptionDTO);

        try {
            this.subscriptionServiceBlockingStub.newSubscription(subscription);
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }
    }

    public void deleteSubscription(UUID uuid) {
        try {
            this.subscriptionServiceBlockingStub.deleteSubscription(SubscriptionId.newBuilder().setId(String.valueOf(uuid)).build());
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }
    }
}
