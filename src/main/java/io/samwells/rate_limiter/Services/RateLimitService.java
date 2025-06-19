package io.samwells.rate_limiter.Services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.samwells.rate_limiter.Algorithms.IQueueingLimitAlgorithm;
import io.samwells.rate_limiter.Algorithms.IRateLimitAlgorithm;
import io.samwells.rate_limiter.Models.IBackgroundJob;
import io.samwells.rate_limiter.Models.RateLimitAlgorithm;
import io.samwells.rate_limiter.Models.Exceptions.UnsupportedAlgorithmException;

@Service
public class RateLimitService implements IRateLimitService {
    private final Map<RateLimitAlgorithm, IRateLimitAlgorithm> algorithms;
    private final Map<RateLimitAlgorithm, IQueueingLimitAlgorithm> queueingAlgorithms;

    public RateLimitService(List<IRateLimitAlgorithm> algorithms, List<IQueueingLimitAlgorithm> queueingAlgorithms) {
        this.algorithms = algorithms.stream().collect(Collectors.toMap(algorithm -> algorithm.getAlgorithm(), algorithm -> algorithm));
        this.queueingAlgorithms = queueingAlgorithms.stream().collect(Collectors.toMap(algorithm -> algorithm.getAlgorithm(), algorithm -> algorithm));
    }
    
    @Override
    public boolean isRateLimited(String key, RateLimitAlgorithm algorithm) {
        if (!this.algorithms.containsKey(algorithm)) throw new UnsupportedAlgorithmException(algorithm);

        return this.algorithms.get(algorithm).isRateLimited(key);
    }

    @Override
    public <T>boolean isRateLimited(String key, RateLimitAlgorithm algorithm, IBackgroundJob<T> job) {
        if (!this.queueingAlgorithms.containsKey(algorithm)) throw new UnsupportedAlgorithmException(algorithm);

        return this.queueingAlgorithms.get(algorithm).isRateLimited(key, job.serialize());
    }
}
