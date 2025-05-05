package it.polimi.it.galaxytrucker.networking;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {
    // Create a single global scheduled executor

    // Singleton ScheduledExecutorService
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Private constructor to prevent instantiation
    private Timer() {}

    // Schedule a repeating task
    public static void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    // Schedule a one-time task
    public static void scheduleOnce(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(task, delay, unit);
    }

    // Optional: shutdown
    public static void shutdown() {
        scheduler.shutdown();
    }
}
