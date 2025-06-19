package io.samwells.rate_limiter.Services;

import java.time.Instant;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LeakyBucketProcessingService {
    private final RedisTemplate<String, String> redisTemplate;

    public LeakyBucketProcessingService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // THIS IS A POOR IMPLEMENTATION AND IS PURELY TO TEST REMOVING FROM REDIS/LEAKY BUCKET:
        // It is blocking and not performant (keys() is a blocking operation)
        // Jobs could be executed multiple times in a distributed environment due to no locking
        // We aren't setting a limit on the number of jobs to process in a single batch
    // FixedRate should ideally match dripIntervalInMs used in LeakyBucket
    @Scheduled(fixedRate = 100)
    public void process() {
        var currentTimeInMs = Instant.now().toEpochMilli();
        // This is blocking and not performant
        var keys = redisTemplate.keys("rate_limit:leaky:*");

        for (String key : keys) {
            Set<String> jobs = redisTemplate
            .opsForZSet()
            .rangeByScore(key, 0, currentTimeInMs);

            if (jobs == null) continue;

            for (String job : jobs) {
                // Execute job
                System.out.println("Executing job: " + job);
                // Remove job from queue
                redisTemplate.opsForZSet().remove(key, job);
            }
        }
    }
}
