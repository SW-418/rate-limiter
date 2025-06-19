package io.samwells.rate_limiter.Models;

public interface IBackgroundJob<T> {
    String serialize();
}
