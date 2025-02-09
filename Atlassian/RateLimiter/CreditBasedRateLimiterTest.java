package ratelimiter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreditBasedRateLimiterTest {
    private CreditBasedRateLimiter rateLimiter;

    @BeforeEach
    public void setUp() {
        rateLimiter = new CreditBasedRateLimiter(5, 2, 1000);
        rateLimiter.addClient("client1", 10);
    }

    @Test
    public void testAllowRequest() {
        assertTrue(rateLimiter.allowRequest("client1"));
        assertEquals(9, rateLimiter.getRemainingCredits("client1"));
    }

    @Test
    public void testAddCredits() {
        rateLimiter.addCredits("client1", 5);
        assertEquals(15, rateLimiter.getRemainingCredits("client1"));
    }

    @Test
    public void testGetRemainingCredits() {
        assertEquals(10, rateLimiter.getRemainingCredits("client1"));
    }

    @Test
    public void testDenyRequestWhenNoCredits() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.allowRequest("client1");
        }
        assertFalse(rateLimiter.allowRequest("client1"));
    }
}
