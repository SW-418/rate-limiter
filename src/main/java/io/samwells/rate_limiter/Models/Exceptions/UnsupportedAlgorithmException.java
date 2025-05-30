package io.samwells.rate_limiter.Models.Exceptions;

import io.samwells.rate_limiter.Models.RateLimitAlogirthm;

public class UnsupportedAlgorithmException extends RuntimeException {
    public UnsupportedAlgorithmException(RateLimitAlogirthm algorithm) {
        super("Unsupported algorithm: " + algorithm);
    }
}
