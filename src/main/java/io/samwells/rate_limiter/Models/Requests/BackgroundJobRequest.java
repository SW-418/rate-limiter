package io.samwells.rate_limiter.Models.Requests;

import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

public record BackgroundJobRequest(@NonNull String url, @NonNull HttpMethod method) {}
