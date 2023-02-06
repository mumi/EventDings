package io.av360.eventdings.dispatcher;

import io.av360.eventdings.grpc.SubscriptionGrpc;
import io.av360.eventdings.grpc.HelloRequest;
import io.av360.eventdings.grpc.HelloReply;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerService extends SubscriptionGrpc.SubscriptionImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}