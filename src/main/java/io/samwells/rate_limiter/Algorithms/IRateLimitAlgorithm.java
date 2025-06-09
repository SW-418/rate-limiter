package io.samwells.rate_limiter.Algorithms;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

public interface IRateLimitAlgorithm {
    boolean isRateLimited(String key);
    RateLimitAlgorithm getAlgorithm();
}
