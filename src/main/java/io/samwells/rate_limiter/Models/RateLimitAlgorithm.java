package io.samwells.rate_limiter.Models;

public enum RateLimitAlgorithm {
    LEAKY_BUCKET,
    TOKEN_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW
}
