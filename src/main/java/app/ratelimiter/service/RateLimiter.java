package app.ratelimiter.service;

public interface RateLimiter {
    boolean tryConsume(String clientKey);
}
