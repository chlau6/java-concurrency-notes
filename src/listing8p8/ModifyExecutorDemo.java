package listing8p8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyExecutorDemo extends Thread {
    ExecutorService exec = Executors.newCachedThreadPool();

    public void setCorePoolSize(int size) {
        if (exec instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) exec).setCorePoolSize(10);
        } else {
            throw new AssertionError("Oops, bad assumption");
        }
    }
}

/*
Most of the options passed to the ThreadPoolExecutor constructors can also be modified after construction via setters
(such as the core thread pool size, maximum thread pool size, keep-alive time, thread factory,
and rejected execution handler).
If the Executor is created through one of the factory methods in Executors (except newSingleThreadExecutor),
you can cast the result to Thread- PoolExecutor to access the setters as in Listing 8.8.
*/
