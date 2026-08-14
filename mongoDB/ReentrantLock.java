public class ReentrantLock {
    private Thread currentOwner = null;
    private int holdCount = 0;
    private final boolean isFair;
    private final Queue<Thread> waitQueue = new LinkedList<>();

    public ReentrantLock(boolean fair) {
        this.isFair = fair;
    }

    // Acquire the lock
    public synchronized void lock() throws InterruptedException {
        Thread callingThread = Thread.currentThread();
        
        // Reentrancy logic: Same thread, increment hold count
        while (isLocked() && currentOwner != callingThread) {
            wait();
        }
        
        holdCount++;
        currentOwner = callingThread;
    }

    // Release the lock
    public synchronized void unlock() {
        if (Thread.currentThread() != this.currentOwner) {
            throw new IllegalMonitorStateException("Calling thread does not hold the lock");
        }
        
        holdCount--;
        
        // Only unlock if hold count reaches 0
        if (holdCount == 0) {
            currentOwner = null;
            notify(); // Wake up waiting threads
        }
    }

    public synchronized boolean isLocked() {
        return holdCount > 0;
    }
}
