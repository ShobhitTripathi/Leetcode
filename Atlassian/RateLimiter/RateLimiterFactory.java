package ratelimiter;

public class RateLimiterFactory {

    // this is for the on using Services
    public static RateLimiter getRateLimiter(String type, int maxRequests, long windowSizeInMillis) {
        switch (type) {
            case "FIXED_WINDOW":
                return new FixedWindowRateLimiter(maxRequests, windowSizeInMillis);
            case "SLIDING_WINDOW":
                return new SlidingWindowRateLimiter(maxRequests, windowSizeInMillis);
            case "BUCKET":
                return new TokenBucketRateLimiter(maxRequests, windowSizeInMillis);
            default:
                throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        }
    }

    // this is for the one using Singletone pattern
    public static RateLimiter createRateLimiter(String type, int maxRequests, long windowSizeInMillis) {
        switch (type.toLowerCase()) {
            case "fixed":
                return new FixedWindowRateLimiter(maxRequests, windowSizeInMillis);
            case "sliding":
                return new SlidingWindowRateLimiter(maxRequests, windowSizeInMillis);
            case "bucket":
                return new TokenBucketRateLimiter(maxRequests, windowSizeInMillis);
            default:
                throw new IllegalArgumentException("Unknown rate limiter type");
        }
    }
}
