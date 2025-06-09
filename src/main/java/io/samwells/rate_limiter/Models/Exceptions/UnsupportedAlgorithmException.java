package io.samwells.rate_limiter.Models.Exceptions;

import io.samwells.rate_limiter.Models.RateLimitAlgorithm;

public class UnsupportedAlgorithmException extends RuntimeException {
    public UnsupportedAlgorithmException(RateLimitAlgorithm algorithm) {
        super("Unsupported algorithm: " + algorithm);
    }
}
