package io.samwells.rate_limiter.Config;

import java.time.temporal.ChronoUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.samwells.rate_limiter.Algorithms.FixedWindow;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class AlgorithmConfig {
    @Bean
    public FixedWindow fixedWindow(RedisTemplate<String, Integer> redisTemplate) {
        return new FixedWindow(ChronoUnit.MINUTES, 10, redisTemplate);
    }
}
