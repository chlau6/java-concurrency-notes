package listing8p7;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyAppThread extends Thread {
    public static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t,
                                                  Throwable e) {
                        log.log(Level.SEVERE,
                                "UNCAUGHT in thread " + t.getName(), e);
                    }
                });
    }

    public void run() {
        // Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifecycle;

        if (debug) {
            log.log(Level.FINE, "Created "+getName());
        }

        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) {
                log.log(Level.FINE, "Exiting "+getName());
            }
        }
    }
    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }
}

/*
Whenever a thread pool needs to create a thread, it does so through a thread factory (see Listing 8.5).
The default thread factory creates a new, nondaemon thread with no special configuration.
Specifying a thread factory allows you to customize the configuration of pool threads.
ThreadFactory has a single method, newThread, that is called whenever a thread pool needs to create a new thread.
*/
