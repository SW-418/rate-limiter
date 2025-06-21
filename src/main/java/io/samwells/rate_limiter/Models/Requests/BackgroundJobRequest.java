package io.samwells.rate_limiter.Models.Requests;

import org.springframework.http.HttpMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BackgroundJobRequest(@NotBlank String url, @NotNull HttpMethod method) {}
