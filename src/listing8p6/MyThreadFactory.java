package listing8p6;

import dummy.MyAppThread;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName);
    }
}

/*
Whenever a thread pool needs to create a thread, it does so through a thread factory (see Listing 8.5).
The default thread factory creates a new, nondaemon thread with no special configuration.
Specifying a thread factory allows you to customize the configuration of pool threads.
ThreadFactory has a single method, newThread, that is called whenever a thread pool needs to create a new thread.
*/
