package io.samwells.rate_limiter.Models.Exceptions;

import java.time.temporal.ChronoUnit;

public class UnsupportedIntervalException extends RuntimeException {
    public UnsupportedIntervalException(ChronoUnit interval) {
        super("Unsupported interval: " + interval);
    }
}
