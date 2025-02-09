package ratelimiter;

public class Main {
//    public static void main(String[] args) {
//        RateLimiter fixedWindowRateLimiter = RateLimiterFactory.createRateLimiter("fixed", 10, 60000);
//        RateLimiter slidingWindowRateLimiter = RateLimiterFactory.createRateLimiter("sliding", 10, 60000);
//
//        // Testing Fixed Window Rate Limiter
//        System.out.println("Fixed Window Rate Limiter:");
//        for (int i = 0; i < 12; i++) {
//            System.out.println(fixedWindowRateLimiter.allowRequest("client1"));
//        }
//
//        // Testing Sliding Window Rate Limiter
//        System.out.println("Sliding Window Rate Limiter:");
//        for (int i = 0; i < 12; i++) {
//            System.out.println(slidingWindowRateLimiter.allowRequest("client2"));
//        }
//    }

    // Example with Bucket Window
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter("BUCKET", 5, 1000);
        RateLimiterService service = new RateLimiterService(rateLimiter);

        String userId = "user1";

        for (int i = 1; i <= 10; i++) {
            System.out.println("Request " + i + ": " + (service.isAllowed(userId) ? "Allowed" : "Blocked"));
        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        // Initialize rate limiter
//        CreditBasedRateLimiter rateLimiter = new CreditBasedRateLimiter(5, 2, 1);
//
//        // Add clients
//        rateLimiter.addClient("client1", 10); // 10 initial credits
//        rateLimiter.addClient("client2", 5);  // 5 initial credits
//
//        // Simulate API requests
//        for (int i = 0; i < 20; i++) {
//            System.out.println("Request " + i + " for client1: " + (rateLimiter.allowRequest("client1") ? "Allowed" : "Denied"));
//            Thread.sleep(500); // Simulate 500ms delay between requests
//        }
//
//        // Add credits
//        rateLimiter.addCredits("client1", 5);
//        System.out.println("Added 5 credits to client1. Remaining credits: " + rateLimiter.getRemainingCredits("client1"));
//
//        // Stop the scheduler
//        rateLimiter.stop();
//    }
}
