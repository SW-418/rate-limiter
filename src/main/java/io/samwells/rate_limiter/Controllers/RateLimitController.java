package io.samwells.rate_limiter.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;
import io.samwells.rate_limiter.Models.Requests.BackgroundJobRequest;
import io.samwells.rate_limiter.Services.IRateLimitService;
import jakarta.validation.Valid;
import io.samwells.rate_limiter.Models.HttpBackgroundJob;

@RestController
@RequestMapping("/api/v1/rate-limit")
class RateLimitController {

    private final IRateLimitService rateLimitService;

    public RateLimitController(IRateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @GetMapping("/fixed-window")
    // Header is just used as an easy way to test different users, this would normally be IP or user id defined by JWT or similar
    public ResponseEntity<String> fixedWindow(@RequestHeader("X-User-Id") String userId) {
        if (!rateLimitService.isRateLimited(userId, RateLimitAlgorithm.FIXED_WINDOW)) return ResponseEntity.ok("ok");

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .build();
    }

    @GetMapping("/sliding-window")
    // Header is just used as an easy way to test different users, this would normally be IP or user id defined by JWT or similar
    public ResponseEntity<String> slidingWindow(@RequestHeader("X-User-Id") String userId) {
        if (!rateLimitService.isRateLimited(userId, RateLimitAlgorithm.SLIDING_WINDOW)) return ResponseEntity.ok("ok");

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .build();
    }

    @GetMapping("/token-bucket")
    // Header is just used as an easy way to test different users, this would normally be IP or user id defined by JWT or similar
    public ResponseEntity<String> tokenBucket(@RequestHeader("X-User-Id") String userId) {
        if (!rateLimitService.isRateLimited(userId, RateLimitAlgorithm.TOKEN_BUCKET)) return ResponseEntity.ok("ok");

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .build();
    }

    // POST is used here as leaky bucket is a stateful algorithm that can result in longer response times
    @PostMapping("/leaky-bucket")
    // Header is just used as an easy way to test different users, this would normally be IP or user id defined by JWT or similar
    public ResponseEntity<String> leakyBucket(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody BackgroundJobRequest request) {
        if (!rateLimitService.isRateLimited(userId, RateLimitAlgorithm.LEAKY_BUCKET, new HttpBackgroundJob(request))) return ResponseEntity.accepted().build();

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .build();
    }
}
