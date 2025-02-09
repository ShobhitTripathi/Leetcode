package ratelimiter;

public class RateLimiterTask implements Runnable {
    private final RateLimiterService service;
    private final String userId;

    public RateLimiterTask(RateLimiterService service, String userId) {
        this.service = service;
        this.userId = userId;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + " - Request " + i + ": " + (service.isAllowed(userId) ? "Allowed" : "Blocked"));
        }
    }
}
