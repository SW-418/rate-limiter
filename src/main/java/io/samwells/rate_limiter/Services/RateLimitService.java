package io.samwells.rate_limiter.Services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.samwells.rate_limiter.Algorithms.IRateLimitAlgorithm;
import io.samwells.rate_limiter.Models.RateLimitAlogirthm;
import io.samwells.rate_limiter.Models.Exceptions.UnsupportedAlgorithmException;

@Service
public class RateLimitService implements IRateLimitService {
    private final Map<RateLimitAlogirthm, IRateLimitAlgorithm> algorithms;

    public RateLimitService(List<IRateLimitAlgorithm> algorithms) {
        this.algorithms = algorithms.stream().collect(Collectors.toMap(algorithm -> algorithm.getAlgorithm(), algorithm -> algorithm));
    }
    
    @Override
    public boolean isRateLimited(String key, RateLimitAlogirthm algorithm) {
        if (!this.algorithms.containsKey(algorithm)) throw new UnsupportedAlgorithmException(algorithm);

        return this.algorithms.get(algorithm).isRateLimited(key);
    }
}
