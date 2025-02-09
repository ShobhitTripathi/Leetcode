package ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter implements RateLimiter {
    private final Map<String, Long> tokens;
    private final Map<String, Long> lastUpdated;
    private final long maxTokens;
    private final long refillRatePerMillis;

    public TokenBucketRateLimiter(long maxTokens, long refillRatePerMillis) {
        this.maxTokens = maxTokens;
        this.refillRatePerMillis = refillRatePerMillis;
        this.tokens = new ConcurrentHashMap<>();
        this.lastUpdated = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();

        // Initialize token bucket for the client if not present
        tokens.putIfAbsent(clientId, maxTokens);
        lastUpdated.putIfAbsent(clientId, currentTime);

        // Calculate elapsed time since last update
        long lastUpdateTime = lastUpdated.get(clientId);
        long elapsedMillis = currentTime - lastUpdateTime;

        // Calculate how many tokens to refill
        long refillTokens = elapsedMillis / refillRatePerMillis;
        if (refillTokens > 0) {
            long currentTokens = tokens.get(clientId);
            long newTokenCount = Math.min(maxTokens, currentTokens + refillTokens);
            tokens.put(clientId, newTokenCount);
            lastUpdated.put(clientId, currentTime); // Update last updated time
        }

        // Check if there's at least one token available
        if (tokens.get(clientId) > 0) {
            tokens.put(clientId, tokens.get(clientId) - 1); // Deduct a token
            return true;
        }
        return false; // Reject request if no tokens are available
    }
}
