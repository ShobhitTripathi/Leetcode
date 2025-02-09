package ratelimiter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenBucketRateLimiterTest {
    private TokenBucketRateLimiter rateLimiter;

    @BeforeEach
    public void setUp() {
        rateLimiter = new TokenBucketRateLimiter(5, 2, 1000);
    }

    @Test
    public void testAllowRequest() {
        assertTrue(rateLimiter.allowRequest("client1"));
    }

    @Test
    public void testDenyRequestWhenNoTokens() {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest("client1"));
        }
        assertFalse(rateLimiter.allowRequest("client1"));
    }

    @Test
    public void testTokenRefill() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest("client1"));
        }
        assertFalse(rateLimiter.allowRequest("client1"));

        // Wait for tokens to refill
        Thread.sleep(2000);

        assertTrue(rateLimiter.allowRequest("client1"));
    }
}