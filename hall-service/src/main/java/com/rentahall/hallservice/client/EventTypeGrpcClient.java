package com.rentahall.hallservice.client;

import com.google.protobuf.Empty;
import com.rentahall.eventtype.grpc.EventTypeList;
import com.rentahall.eventtype.grpc.EventTypeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class EventTypeGrpcClient {
    private final EventTypeServiceGrpc.EventTypeServiceBlockingStub stub;

    public EventTypeGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8087)   // event-service
                .usePlaintext()
                .build();
        this.stub = EventTypeServiceGrpc.newBlockingStub(channel);
    }

    public EventTypeList getAllEventTypes() {
        return stub.getAllEventTypes(Empty.getDefaultInstance());
    }
}
