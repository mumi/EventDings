package io.av360.eventdings.dispatcher;

import io.av360.eventdings.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerService extends SubscriptionServiceGrpc.SubscriptionServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void newSubscription(Subscription request, StreamObserver<SubscriptionId> responseObserver) {
        System.out.println("newSubscription");
        System.out.println(request);
        SubscriptionId reply = SubscriptionId.newBuilder()
                .setId(request.getId())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteSubscription(SubscriptionId request, StreamObserver<SubscriptionId> responseObserver) {
        System.out.println("deleteSubscription");
        System.out.println(request);
        SubscriptionId reply = SubscriptionId.newBuilder()
                .setId(request.getId())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}