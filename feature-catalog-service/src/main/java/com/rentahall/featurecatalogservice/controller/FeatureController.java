package com.rentahall.featurecatalogservice.controller;

import com.rentahall.featurecatalogservice.dto.FeatureRequest;
import com.rentahall.featurecatalogservice.dto.FeatureResponse;
import com.rentahall.featurecatalogservice.service.FeatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @PostMapping
    public ResponseEntity<FeatureResponse> createFeature(@Valid @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.createFeature(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureResponse> getFeatureById(@PathVariable UUID id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<FeatureResponse> getFeatureByName(@PathVariable String name) {
        return ResponseEntity.ok(featureService.getFeatureByName(name));
    }

    @GetMapping
    public ResponseEntity<List<FeatureResponse>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureResponse> updateFeature(@PathVariable UUID id, @Valid @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.updateFeature(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable UUID id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}
