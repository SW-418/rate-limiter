package io.samwells.rate_limiter.Algorithms;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.samwells.rate_limiter.Models.RateLimitAlogirthm;

@Component
public class SlidingWindow implements IRateLimitAlgorithm {
    private final ChronoUnit interval;
    private final int limit;
    private final StringRedisTemplate redisTemplate;

    public SlidingWindow(ChronoUnit interval, int limit, StringRedisTemplate redisTemplate) {  
        this.interval = interval;
        this.limit = limit;
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public boolean isRateLimited(String key) {
        String rateLimitKey = calculateKey(key);
        var now = Instant.now().toEpochMilli();
        var windowStart = calculateWindowStart(now);

        // Remove old entries
        redisTemplate.opsForZSet().removeRangeByScore(rateLimitKey, 0, windowStart);

        // Retrieve and check counts within window
        var count = redisTemplate.opsForZSet().count(rateLimitKey, windowStart, now);
        if (count == null || count >= this.limit) return true;

        // Add entry if space in window
        redisTemplate.opsForZSet().add(rateLimitKey, String.valueOf(now), now);
        
        return false;
    }

    @Override
    public RateLimitAlogirthm getAlgorithm() {
        return RateLimitAlogirthm.SLIDING_WINDOW;
    }

    private String calculateKey(String key) {
        return "rate_limit:sliding:" + key;
    }

    private Long calculateWindowStart(long now) {
        return now - this.interval.getDuration().toMillis();
    }
}
