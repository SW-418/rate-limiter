package io.samwells.rate_limiter.Config;

import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.samwells.rate_limiter.Algorithms.FixedWindow;
import io.samwells.rate_limiter.Algorithms.SlidingWindow;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class AlgorithmConfig {
    @Bean
    public FixedWindow fixedWindow(RedisTemplate<String, Integer> redisTemplate) {
        return new FixedWindow(ChronoUnit.MINUTES, 10, redisTemplate);
    }
    
    @Bean
    public SlidingWindow slidingWindow(StringRedisTemplate redisTemplate) {
        return new SlidingWindow(ChronoUnit.MINUTES, 10, redisTemplate);
    }
}
