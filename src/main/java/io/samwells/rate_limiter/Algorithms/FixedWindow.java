package io.samwells.rate_limiter.Algorithms;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;
import io.samwells.rate_limiter.Models.Exceptions.UnsupportedIntervalException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class FixedWindow implements IRateLimitAlgorithm {
    private final ChronoUnit interval;
    private final int limit;
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> script;

    public FixedWindow(ChronoUnit interval, int limit, StringRedisTemplate redisTemplate, RedisScript<Long> script) {  
        this.interval = interval;
        this.limit = limit;
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    @Override
    public boolean isRateLimited(String key) {
        String redisKey = calculateKey(key);

        long result = this.redisTemplate.execute(
            this.script,
            List.of(redisKey),
            String.valueOf(this.limit),
            String.valueOf(this.interval.getDuration().toSeconds())
        );
        
        return result == 0;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.FIXED_WINDOW;
    }

    private String calculateKey(String key) {
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
