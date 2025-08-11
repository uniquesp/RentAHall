package com.rentahall.featurecatalogservice.service.impl;

import com.rentahall.featurecatalogservice.dto.FeatureRequest;
import com.rentahall.featurecatalogservice.dto.FeatureResponse;
import com.rentahall.featurecatalogservice.entity.Feature;
import com.rentahall.featurecatalogservice.exception.ResourceNotFoundException;
import com.rentahall.featurecatalogservice.repository.FeatureRepository;
import com.rentahall.featurecatalogservice.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    @Override
    public FeatureResponse createFeature(FeatureRequest request) {
        Feature feature = Feature.builder()
                .name(request.getName().toLowerCase()) // store lowercase
                .build();
        Feature saved = featureRepository.save(feature);
        return mapToResponse(saved);
    }

    @Override
    public FeatureResponse getFeatureById(UUID id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id " + id));
        return mapToResponse(feature);
    }

    @Override
    public FeatureResponse getFeatureByName(String name) {
        Feature feature = featureRepository.findByName(name.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with name " + name));
        return mapToResponse(feature);
    }

    @Override
    public List<FeatureResponse> getAllFeatures() {
        return featureRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FeatureResponse updateFeature(UUID id, FeatureRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found with id " + id));

        feature.setName(request.getName().toLowerCase());
        Feature updated = featureRepository.save(feature);
        return mapToResponse(updated);
    }

    @Override
    public void deleteFeature(UUID id) {
        if (!featureRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feature not found with id " + id);
        }
        featureRepository.deleteById(id);
    }

    private FeatureResponse mapToResponse(Feature feature) {
        return FeatureResponse.builder()
                .id(feature.getId())
                .name(feature.getName())
                .build();
    }
}