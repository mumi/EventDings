package io.av360.eventdings.subscribing.grpc;

import com.google.protobuf.Timestamp;
import io.av360.eventdings.lib.grpc.*;
import io.av360.eventdings.lib.dtos.SubscriptionDTO;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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


    public void sendSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionDTOToGrpcSubscription(subscriptionDTO);

        try {
            this.subscriptionServiceBlockingStub.newSubscription(subscription);
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }
    }

    public List<SubscriptionId> sendSubscriptions(List<SubscriptionDTO> subscriptionDTOs) {
        List<SubscriptionId> subscriptionIds = new ArrayList<SubscriptionId>();
        try {
            StreamObserver<SubscriptionId> responseObserver = new StreamObserver<SubscriptionId>() {
                @Override
                public void onNext(SubscriptionId subscriptionId) {
                    subscriptionIds.add(subscriptionId);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("RPC failed: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                }
            };

            StreamObserver<Subscription> requestObserver = this.subscriptionServiceStub.newSubscriptions(responseObserver);
            for (SubscriptionDTO subscriptionDTO : subscriptionDTOs) {
                requestObserver.onNext(subscriptionDTOToGrpcSubscription(subscriptionDTO));
            }
            requestObserver.onCompleted();
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }
        return subscriptionIds;
    }

    public void deleteSubscription(UUID id) {
        try {
            this.subscriptionServiceBlockingStub.deleteSubscription(SubscriptionId.newBuilder().setId(String.valueOf(id)).build());
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }
    }

    public List<SubscriptionId> deleteSubscriptions(List<UUID> ids) {
        List<SubscriptionId> subscriptionIds = new ArrayList<SubscriptionId>();
        try {
            StreamObserver<SubscriptionId> responseObserver = new StreamObserver<SubscriptionId>() {
                @Override
                public void onNext(SubscriptionId subscriptionId) {
                    subscriptionIds.add(subscriptionId);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("RPC failed: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                }
            };

            StreamObserver<SubscriptionId> requestObserver = this.subscriptionServiceStub.deleteSubscriptions(responseObserver);
            for (UUID id : ids) {
                requestObserver.onNext(SubscriptionId.newBuilder().setId(String.valueOf(id)).build());
            }
            requestObserver.onCompleted();
        } catch (final StatusRuntimeException e) {
            log.error("RPC failed: " + e.getStatus());
        }

        return subscriptionIds;
    }
}
