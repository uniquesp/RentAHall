package com.rentahall.featurecatalogservice.service;

import com.rentahall.featurecatalogservice.dto.FeatureRequest;
import com.rentahall.featurecatalogservice.dto.FeatureResponse;

import java.util.List;
import java.util.UUID;

public interface FeatureService {
    FeatureResponse createFeature(FeatureRequest request);
    FeatureResponse getFeatureById(UUID id);
    FeatureResponse getFeatureByName(String name);
    List<FeatureResponse> getAllFeatures();
    FeatureResponse updateFeature(UUID id, FeatureRequest request);
    void deleteFeature(UUID id);
}
