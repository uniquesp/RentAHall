package com.rentahall.featurecatalogservice.grpc;

import com.google.protobuf.Empty;
import com.rentahall.featurecatalogservice.entity.FeatureEntity;
import com.rentahall.featurecatalogservice.repository.FeatureRepository;
import com.rentahall.featurecatlogservice.grpc.Feature;
import com.rentahall.featurecatlogservice.grpc.FeatureId;
import com.rentahall.featurecatlogservice.grpc.FeatureList;
import com.rentahall.featurecatlogservice.grpc.FeatureServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class FeatureGrpcService extends FeatureServiceGrpc.FeatureServiceImplBase {

    private final FeatureRepository repository;

    @Override
    public void getAllFeatures(Empty request, StreamObserver<FeatureList> responseObserver) {
        List<FeatureEntity> entities = repository.findAll();

        // Convert entities -> proto
        List<Feature> protoFeatures = entities.stream()
                .map(this::toProto)
                .collect(Collectors.toList());

        FeatureList response = FeatureList.newBuilder()
                .addAllFeatures(protoFeatures)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFeatureById(FeatureId request, StreamObserver<Feature> responseObserver) {
        FeatureEntity entity = repository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        Feature proto = toProto(entity);

        responseObserver.onNext(proto);
        responseObserver.onCompleted();
    }

    private Feature toProto(FeatureEntity entity) {
        return Feature.newBuilder()
                .setId(entity.getId().toString())
                .setName(entity.getName())
                .build();
    }
}
