package io.samwells.rate_limiter.Config;

import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.samwells.rate_limiter.Algorithms.FixedWindow;
import io.samwells.rate_limiter.Algorithms.SlidingWindow;
import io.samwells.rate_limiter.Algorithms.TokenBucket;
import io.samwells.rate_limiter.Algorithms.LeakyBucket;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AlgorithmConfig {
    @Bean
    public FixedWindow fixedWindow(StringRedisTemplate redisTemplate, RedisScript<Long> fixedWindowScript) {
        return new FixedWindow(ChronoUnit.MINUTES, 10, redisTemplate, fixedWindowScript);
    }
    
    @Bean
    public SlidingWindow slidingWindow(StringRedisTemplate redisTemplate, RedisScript<Long> slidingWindowScript) {
        return new SlidingWindow(ChronoUnit.MINUTES, 10, redisTemplate, slidingWindowScript);
    }

    @Bean
    public TokenBucket tokenBucket(StringRedisTemplate redisTemplate, RedisScript<Long> tokenBucketScript) {
        return new TokenBucket(
            10,
            ChronoUnit.SECONDS,
            1,
            redisTemplate,
            tokenBucketScript
        );
    }

    @Bean
    public LeakyBucket leakyBucket(StringRedisTemplate redisTemplate, RedisScript<Long> leakyBucketScript) {
        return new LeakyBucket(
            10,
            100,
            redisTemplate,
            leakyBucketScript
        );
    }

    @Bean
    public RedisScript<Long> fixedWindowScript() {
        return script("scripts/fixedWindowRateLimit.lua");
    }

    @Bean
    public RedisScript<Long> slidingWindowScript() {
        return script("scripts/slidingWindowRateLimit.lua");
    }

    @Bean
    public RedisScript<Long> tokenBucketScript() {
        return script("scripts/tokenRateLimit.lua");
    }

    @Bean
    public RedisScript<Long> leakyBucketScript() {
        return script("scripts/leakyBucketRateLimit.lua");
    }

    private RedisScript<Long> script(String path) {
        return RedisScript.of(new ClassPathResource(path), Long.class);
    }
}
