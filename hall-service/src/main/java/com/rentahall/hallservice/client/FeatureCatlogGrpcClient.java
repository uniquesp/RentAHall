package com.rentahall.hallservice.client;

import com.google.protobuf.Empty;
import com.rentahall.feature.grpc.Feature;
import com.rentahall.feature.grpc.FeatureId;
import com.rentahall.feature.grpc.FeatureList;
import com.rentahall.feature.grpc.FeatureServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class FeatureCatlogGrpcClient {

    private final FeatureServiceGrpc.FeatureServiceBlockingStub stub;

    public FeatureCatlogGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8088)
                .usePlaintext()
                .build();
        this.stub = FeatureServiceGrpc.newBlockingStub(channel);
    }

    public FeatureList getAllFeatures() {
        return stub.getAllFeatures(Empty.getDefaultInstance());
    }

    public Feature getFeatureById(String id) {
        FeatureId request = FeatureId.newBuilder()
                .setId(id)
                .build();
        return stub.getFeatureById(request);
    }
}
