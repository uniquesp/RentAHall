package com.rentahall.eventservice.grpc;

import com.google.protobuf.Empty;
import com.rentahall.eventservice.entity.Event;
import com.rentahall.eventservice.repository.EventRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class EventTypeGrpcService extends EventTypeServiceGrpc.EventTypeServiceImplBase {

    private final EventRepository repository;

    @Override
    public void getAllEventTypes(Empty request, StreamObserver<EventTypeList> responseObserver) {
        List<Event> entities = repository.findAll();

        List<EventType> protoEvents = entities.stream()
                .map(this::toProto)
                .collect(Collectors.toList());

        EventTypeList response = EventTypeList.newBuilder()
                .addAllEventTypes(protoEvents)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private EventType toProto(Event entity) {
        return EventType.newBuilder()
                .setId(entity.getId().toString())
                .setName(entity.getName())
                .build();
    }
}