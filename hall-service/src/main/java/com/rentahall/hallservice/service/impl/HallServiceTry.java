package com.rentahall.hallservice.service.impl;

import com.rentahall.eventtype.grpc.EventTypeList;
import com.rentahall.hallservice.client.EventTypeGrpcClient;
import org.springframework.stereotype.Service;

@Service
public class HallServiceTry {
    private final EventTypeGrpcClient eventClient;

    public HallServiceTry(EventTypeGrpcClient eventClient) {
        this.eventClient = eventClient;
    }

    public EventTypeList getAllEventTypes() {
        return eventClient.getAllEventTypes();
    }
}
