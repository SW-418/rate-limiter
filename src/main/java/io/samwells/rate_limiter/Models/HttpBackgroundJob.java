package io.samwells.rate_limiter.Models;

import java.time.Instant;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.samwells.rate_limiter.Models.Requests.BackgroundJobRequest;

public class HttpBackgroundJob implements IBackgroundJob<BackgroundJobRequest> {
    private final String url;
    private final HttpMethod method;
    private final ObjectMapper objectMapper;
    private final long timestamp;
    
    public HttpBackgroundJob(BackgroundJobRequest jobData) {
        this.url = jobData.url();
        this.method = jobData.method();
        this.objectMapper = new ObjectMapper();
        this.timestamp = Instant.now().toEpochMilli();
    }

    @Override
    public String serialize() {
        try {
            return this.objectMapper.writeValueAsString(new HttpBackgroundJobDto(this.url, this.method.name(), this.timestamp));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private record HttpBackgroundJobDto(String url, String method, long timestamp) {}
}
