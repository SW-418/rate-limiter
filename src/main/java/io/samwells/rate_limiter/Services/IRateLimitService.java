package io.samwells.rate_limiter.Services;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;
import io.samwells.rate_limiter.Models.IBackgroundJob;

public interface IRateLimitService {
    boolean isRateLimited(String key, RateLimitAlgorithm algorithm);
    <T>boolean isRateLimited(String key, RateLimitAlgorithm algorithm, IBackgroundJob<T> job);
}
