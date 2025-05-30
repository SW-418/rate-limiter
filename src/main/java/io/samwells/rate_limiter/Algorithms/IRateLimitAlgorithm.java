package io.samwells.rate_limiter.Algorithms;

import io.samwells.rate_limiter.Models.RateLimitAlogirthm;

public interface IRateLimitAlgorithm {
    boolean isRateLimited(String key);
    RateLimitAlogirthm getAlgorithm();
}
