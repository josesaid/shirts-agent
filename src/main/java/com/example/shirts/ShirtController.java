package com.example.shirts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shirts")
public class ShirtController {

    private final ShirtService shirtService;

    public ShirtController(ShirtService shirtService) {
        this.shirtService = shirtService;
    }

    @PostMapping("/recommend")
    public ResponseEntity<Shirt> recommend(@RequestBody ShirtRequest request) {
        return ResponseEntity.ok(shirtService.recommend(request));
    }
}
