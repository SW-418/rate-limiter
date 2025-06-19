package io.samwells.rate_limiter.Algorithms;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

import java.util.List;
import java.time.Instant;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

public class LeakyBucket implements IQueueingLimitAlgorithm {
    private int bucketSize;
    private long dripIntervalInMs;
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> script;
    
    public LeakyBucket(int bucketSize, long dripIntervalInMs, StringRedisTemplate redisTemplate, RedisScript<Long> script) {
        this.bucketSize = bucketSize;
        this.dripIntervalInMs = dripIntervalInMs;
        this.redisTemplate = redisTemplate;
        this.script = script;
    }
    
    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.LEAKY_BUCKET;
    }

    @Override
    public boolean isRateLimited(String key, String job) {
        var rateLimitKey = calculateKey(key);
        var currentTimeInMs = Instant.now().toEpochMilli();
        var dripTimeInMs = currentTimeInMs + this.dripIntervalInMs;

        long result = redisTemplate.execute(
            this.script, 
            List.of(rateLimitKey), 
            String.valueOf(this.bucketSize),
            String.valueOf(dripTimeInMs),
            job
        );
        
        return result == 0;
    }

    private String calculateKey(String key) {
        return "rate_limit:leaky:" + key;
    }
}
