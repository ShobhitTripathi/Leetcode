package ratelimiter;

public class RateLimiterService {
    private final RateLimiter rateLimiter;

    public RateLimiterService(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public boolean isAllowed(String userId) {
        return rateLimiter.allowRequest(userId);
    }
}
