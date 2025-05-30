package io.samwells.rate_limiter.Services;

import io.samwells.rate_limiter.Models.RateLimitAlogirthm;

public interface IRateLimitService {
    boolean isRateLimited(String key, RateLimitAlogirthm algorithm);
}
