package io.samwells.rate_limiter.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rate-limit")
class RateLimitController {

    @GetMapping("/limit")
    public ResponseEntity<String> limit() {
        return ResponseEntity.ok("limit");
    }
}
