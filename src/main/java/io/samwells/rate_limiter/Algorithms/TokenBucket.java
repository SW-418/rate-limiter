package io.samwells.rate_limiter.Algorithms;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Component;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

@Component
public class TokenBucket implements IRateLimitAlgorithm {
    // Max number of allowable tokens
    private final int limit;
    // Interval at which tokens are added
    private final ChronoUnit refreshInterval;
    // Number of tokens added per interval
    private final int tokensPerInterval;
    private final StringRedisTemplate redisTemplate;
    // Lua script required for atomic operation
    private final RedisScript<Long> script;
    
    public TokenBucket(int limit, ChronoUnit refreshInterval, int tokensPerInterval, StringRedisTemplate redisTemplate, RedisScript<Long> script) {
        this.limit = limit;
        this.refreshInterval = refreshInterval;
        this.tokensPerInterval = tokensPerInterval;
        this.redisTemplate = redisTemplate;
        this.script = script;
    }
    
    @Override
    public boolean isRateLimited(String key) {
        String rateLimitKey = "rate_limit:token:" + key;
        long currentTime = Instant.now().toEpochMilli();
        
        return this.isRateLimited(rateLimitKey, currentTime);
    }

    private boolean isRateLimited(String key, long currentTime) {
        long result = this.redisTemplate.execute(
            this.script,
            List.of(key),
            String.valueOf(this.limit),
            String.valueOf(currentTime),
            String.valueOf(this.refreshInterval.getDuration().toMillis()),
            String.valueOf(this.tokensPerInterval)
        );
        
        return result == 0;
    }
    
    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.TOKEN_BUCKET;
    }
}
