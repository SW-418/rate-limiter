package io.samwells.rate_limiter.Models;

public enum RateLimitAlogirthm {
    LEAKY_BUCKET,
    FIXED_TOKEN_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW
}
