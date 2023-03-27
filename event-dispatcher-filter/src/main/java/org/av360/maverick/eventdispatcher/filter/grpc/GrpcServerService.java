package org.av360.maverick.eventdispatcher.filter.grpc;

import org.av360.maverick.eventdispatcher.filter.SubscriptionManager;
import org.av360.maverick.eventdispatcher.shared.grpc.*;
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
        //TODO: Delete all subscriptions before adding new ones (Refreshing). Maybe this should be a new (refreshSubscriptions) method?
        SubscriptionManager.getInstance().deleteAllSubscriptions(false);
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
        responseObserver.onNext(SubscriptionManager.getInstance().deleteSubscription(request, true));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<SubscriptionId> deleteSubscriptions(StreamObserver<SubscriptionId> responseObserver) {
        return new StreamObserver<SubscriptionId>() {

            List<SubscriptionId> replyList = new ArrayList<SubscriptionId>();
            @Override
            public void onNext(SubscriptionId subscriptionId) {
                replyList.add(SubscriptionManager.getInstance().deleteSubscription(subscriptionId, true));
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