package io.samwells.rate_limiter.Algorithms;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

@Component
public class SlidingWindow implements IRateLimitAlgorithm {
    private final ChronoUnit interval;
    private final int limit;
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> script;

    public SlidingWindow(ChronoUnit interval, int limit, StringRedisTemplate redisTemplate, RedisScript<Long> script) {  
        this.interval = interval;
        this.limit = limit;
        this.redisTemplate = redisTemplate;
        this.script = script;
    }
    
    @Override
    public boolean isRateLimited(String key) {
        String rateLimitKey = calculateKey(key);

        var currentTimeInMs = Instant.now().toEpochMilli();
        var windowStartInMs = calculateWindowStart(currentTimeInMs);
        var windowSizeInSeconds = this.interval.getDuration().toSeconds();

        long result = redisTemplate.execute(
            this.script, 
            List.of(rateLimitKey), 
            String.valueOf(this.limit), 
            String.valueOf(currentTimeInMs), 
            String.valueOf(windowStartInMs),
            String.valueOf(windowSizeInSeconds)
        );
        
        return result == 0;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.SLIDING_WINDOW;
    }

    private String calculateKey(String key) {
        return "rate_limit:sliding:" + key;
    }

    private Long calculateWindowStart(long currentTimeInMs) {
        return currentTimeInMs - this.interval.getDuration().toMillis();
    }
}
