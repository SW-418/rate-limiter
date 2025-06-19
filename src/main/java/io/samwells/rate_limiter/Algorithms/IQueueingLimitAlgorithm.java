package io.samwells.rate_limiter.Algorithms;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

public interface IQueueingLimitAlgorithm {
    boolean isRateLimited(String key, String job);
    RateLimitAlgorithm getAlgorithm();
}
