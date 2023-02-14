package io.av360.eventdings.dispatcher.grpc;

import io.av360.eventdings.dispatcher.SubscriptionManager;
import io.av360.eventdings.lib.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class GrpcServerService extends SubscriptionServiceGrpc.SubscriptionServiceImplBase {

    @Override
    public void newSubscription(Subscription request, StreamObserver<SubscriptionId> responseObserver) {
        responseObserver.onNext(SubscriptionManager.getInstance().addSubscription(request));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Subscription> newSubscriptions(StreamObserver<SubscriptionId> responseObserver) {
        return new StreamObserver<Subscription>() {

            List<SubscriptionId> replyList = new ArrayList<SubscriptionId>();
            @Override
            public void onNext(Subscription subscription) {
                replyList.add(SubscriptionManager.getInstance().addSubscription(subscription));
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                replyList.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void deleteSubscription(SubscriptionId request, StreamObserver<SubscriptionId> responseObserver) {
        responseObserver.onNext(SubscriptionManager.getInstance().deleteSubscription(request));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<SubscriptionId> deleteSubscriptions(StreamObserver<SubscriptionId> responseObserver) {
        return new StreamObserver<SubscriptionId>() {

            List<SubscriptionId> replyList = new ArrayList<SubscriptionId>();
            @Override
            public void onNext(SubscriptionId subscriptionId) {
                replyList.add(SubscriptionManager.getInstance().deleteSubscription(subscriptionId));
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                replyList.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}