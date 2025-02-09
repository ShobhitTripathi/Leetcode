package ratelimiter;

public class Main {


    // Example with Bucket Window
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter("BUCKET", 5, 1000);
        RateLimiterService service = new RateLimiterService(rateLimiter);

        String userId = "user1";

        for (int i = 1; i <= 10; i++) {
            System.out.println("Request " + i + ": " + (service.isAllowed(userId) ? "Allowed" : "Blocked"));
        }
    }

    // For running in multiThreaded 
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiterFactory.getRateLimiter("BUCKET", 5, 1000);
        RateLimiterService service = new RateLimiterService(rateLimiter);

        String userId = "user1";

        int numberOfThreads = 3; // Number of threads to run concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(new RateLimiterTask(service, userId));
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}
