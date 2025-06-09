package io.samwells.rate_limiter.Services;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

public interface IRateLimitService {
    boolean isRateLimited(String key, RateLimitAlgorithm algorithm);
}
