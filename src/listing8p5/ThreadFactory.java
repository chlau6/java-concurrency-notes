package listing8p5;

import annotation.ThreadSafe;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

@ThreadSafe
public interface ThreadFactory {
    Thread newThread(Runnable r);
}

/*
Whenever a thread pool needs to create a thread, it does so through a thread factory (see Listing 8.5).
The default thread factory creates a new, nondaemon thread with no special configuration.
Specifying a thread factory allows you to customize the configuration of pool threads.
ThreadFactory has a single method, newThread, that is called whenever a thread pool needs to create a new thread.
*/
