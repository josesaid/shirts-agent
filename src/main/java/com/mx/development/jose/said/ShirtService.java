package com.mx.development.jose.said;

import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ShirtService {

    private static final Set<String> SUPPORTED_ITEMS = Set.of("playera", "shirt", "t-shirt");

    private final ShirtAgent shirtAgent;

    public ShirtService(ShirtAgent shirtAgent) {
        this.shirtAgent = shirtAgent;
    }

    public Shirt recommend(ShirtRequest request) {
        if (request.item() == null || !SUPPORTED_ITEMS.contains(request.item().trim().toLowerCase())) {
            throw new IllegalArgumentException("Unsupported item: " + request.item());
        }
        if (request.color() == null || request.color().isBlank()) {
            throw new IllegalArgumentException("Color is required");
        }
        return shirtAgent.recommend(request.item().trim(), request.color().trim());
    }
}
