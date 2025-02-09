package ratelimiter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CreditBasedRateLimiter implements RateLimiter {
    // Client data structure
    private static class ClientData {
        AtomicInteger tokens;    // Tokens for rate-limiting
        AtomicInteger credits;   // Credits for monetization

        public ClientData(int initialTokens, int initialCredits) {
            this.tokens = new AtomicInteger(initialTokens);
            this.credits = new AtomicInteger(initialCredits);
        }
    }

    // Rate limit and credit cost settings
    private final int maxTokens;          // Maximum tokens per bucket
    private final int refillRate;         // Tokens refilled per second
    private final int creditsPerRequest;  // Credits deducted per request

    private final ConcurrentHashMap<String, ClientData> clientDataMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public CreditBasedRateLimiter(int maxTokens, int refillRate, int creditsPerRequest) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.creditsPerRequest = creditsPerRequest;

        // Start the scheduler to refill tokens every second
        scheduler.scheduleAtFixedRate(this::refillTokens, 0, 1, TimeUnit.SECONDS);
    }

    // Add a new client with initial tokens and credits
    public void addClient(String clientId, int initialCredits) {
        clientDataMap.put(clientId, new ClientData(maxTokens, initialCredits));
    }

    // Check if a request is allowed for a client
    public boolean allowRequest(String clientId) {
        ClientData clientData = clientDataMap.get(clientId);
        if (clientData == null) {
            System.out.println("Client not found: " + clientId);
            return false;
        }

        synchronized (clientData) {
            // Check tokens and credits
            if (clientData.tokens.get() > 0 && clientData.credits.get() >= creditsPerRequest) {
                clientData.tokens.decrementAndGet();           // Deduct one token
                clientData.credits.addAndGet(-creditsPerRequest); // Deduct credits
                return true;
            }
            return false; // Deny if tokens or credits are insufficient
        }
    }

    // Refill tokens for all clients
    private void refillTokens() {
        for (ClientData clientData : clientDataMap.values()) {
            synchronized (clientData) {
                int newTokens = Math.min(maxTokens, clientData.tokens.get() + refillRate);
                clientData.tokens.set(newTokens);
            }
        }
    }

    // Add credits to a client
    public void addCredits(String clientId, int credits) {
        ClientData clientData = clientDataMap.get(clientId);
        if (clientData != null) {
            clientData.credits.addAndGet(credits);
        }
    }

    // Get remaining credits for a client
    public int getRemainingCredits(String clientId) {
        ClientData clientData = clientDataMap.get(clientId);
        return clientData != null ? clientData.credits.get() : 0;
    }

    // Stop the scheduler
    public void stop() {
        scheduler.shutdown();
    }
}

