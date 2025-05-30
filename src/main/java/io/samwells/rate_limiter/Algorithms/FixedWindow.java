package io.samwells.rate_limiter.Algorithms;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.samwells.rate_limiter.Models.RateLimitAlogirthm;
import io.samwells.rate_limiter.Models.Exceptions.UnsupportedIntervalException;

import java.time.Instant;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class FixedWindow implements IRateLimitAlgorithm {
    private final ChronoUnit interval;
    private final int limit;
    private final RedisTemplate<String, Integer> redisTemplate;

    public FixedWindow(ChronoUnit interval, int limit, RedisTemplate<String, Integer> redisTemplate) {  
        this.interval = interval;
        this.limit = limit;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isRateLimited(String key) {
        String redisKey = calculateKey(key);

        var count = redisTemplate
            .opsForValue()
            .increment(redisKey, 1);
        
        // TODO: Throw exception instead of returning true when null
        if (count == null || count > this.limit) return true;

        var timeout = 1;
        redisTemplate.expire(redisKey, Duration.of(timeout, this.interval));
        
        return false;
    }

    @Override
    public RateLimitAlogirthm getAlgorithm() {
        return RateLimitAlogirthm.FIXED_WINDOW;
    }

    private String calculateKey(String key) {
        // TODO: Inject clock object
        var now = Instant.now();
        var second = now.getEpochSecond();
        var minute = second / 60;

        switch (this.interval) {
            case SECONDS:
                return "rate_limit:" + key + ":" + second;
            case MINUTES:
                return "rate_limit:" + key + ":" + minute;
            default:
                throw new UnsupportedIntervalException(this.interval);
        }
    }
}
