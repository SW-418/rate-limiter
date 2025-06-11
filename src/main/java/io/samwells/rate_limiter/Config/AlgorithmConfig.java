package io.samwells.rate_limiter.Config;

import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.samwells.rate_limiter.Algorithms.FixedWindow;
import io.samwells.rate_limiter.Algorithms.SlidingWindow;
import io.samwells.rate_limiter.Algorithms.TokenBucket;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AlgorithmConfig {
    @Bean
    public FixedWindow fixedWindow(StringRedisTemplate redisTemplate) {
        return new FixedWindow(ChronoUnit.MINUTES, 10, redisTemplate, script("scripts/fixedWindowRateLimit.lua"));
    }
    
    @Bean
    public SlidingWindow slidingWindow(StringRedisTemplate redisTemplate) {
        return new SlidingWindow(ChronoUnit.MINUTES, 10, redisTemplate);
    }

    @Bean
    public TokenBucket tokenBucket(StringRedisTemplate redisTemplate, RedisScript<Long> script) {
        return new TokenBucket(
            10,
            ChronoUnit.SECONDS,
            1,
            redisTemplate,
            script("scripts/tokenRateLimit.lua")
        );
    }

    @Bean
    public RedisScript<Long> script(String path) {
        return RedisScript.of(new ClassPathResource(path), Long.class);
    }
}
