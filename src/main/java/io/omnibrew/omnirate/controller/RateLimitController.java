package io.omnibrew.omnirate.controller;

import io.omnibrew.omnirate.model.RateLimitResponse;
import io.omnibrew.omnirate.model.RateLimitRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.omnibrew.omnirate.service.RateLimitService;

@RestController
@RequestMapping("/rate-limit")
public class RateLimitController {
    public final RateLimitService rateLimitService;

    public RateLimitController (RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/check")
    public ResponseEntity<RateLimitResponse> checkRateLimit(@RequestBody RateLimitRequest request) {
        RateLimitResponse response = rateLimitService.checkRateLimit(request);
        if(!response.isAllowed()){
            return ResponseEntity.status(429).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
