package ratelimiter;

public interface RateLimiter {
    boolean allowRequest(String clientId);
}
